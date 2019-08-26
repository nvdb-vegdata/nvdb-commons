package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.core.functional.Optionals;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Join;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Table;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Expression;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function.FieldFunction;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.order.Order;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.projection.Projection;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonEmpty;
import static no.vegvesen.vt.nvdb.commons.core.functional.Functions.castTo;
import static no.vegvesen.vt.nvdb.commons.core.functional.Optionals.mapIfNonNull;
import static no.vegvesen.vt.nvdb.commons.core.functional.Predicates.instanceOf;
import static no.vegvesen.vt.nvdb.commons.core.functional.Predicates.not;
import static no.vegvesen.vt.nvdb.commons.core.lang.StringHelper.isBlank;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Command.SELECT;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect.Capability.LIMIT_OFFSET;

public class SelectStatement extends PreparableStatement {
    private List<Projection> projections;
    private List<Table> tables;
    private List<Join> joins;
    private List<Expression> expressions;
    private List<Field> groups;
    private List<Order> orders;
    private boolean distinct;
    private Integer limit;
    private Integer offset;

    SelectStatement(boolean distinct, List<Projection> projections, List<Table> tables) {
        this.distinct = distinct;
        this.projections = requireNonEmpty(projections, "No projections specified");
        this.tables = requireNonEmpty(tables, "No tables specified");
        this.joins = new LinkedList<>();
        this.expressions = new LinkedList<>();
        this.groups = new LinkedList<>();
        this.orders = new LinkedList<>();
    }

    public SelectStatement join(Join... joins) {
        requireNonEmpty(joins, "No joins specified");
        this.joins.addAll(asList(joins));
        return this;
    }

    @SafeVarargs
    public final SelectStatement joinIf(boolean condition, Supplier<Join>... joinSuppliers) {
        requireNonEmpty(joins, "No join suppliers specified");
        if (condition) {
            Arrays.stream(joinSuppliers).map(Supplier::get).forEach(this.joins::add);
        }
        return this;
    }

    /**
     * Adds one or more expressions to the where clause.
     * @param expressions the expressions to add
     * @return this object, for method chaining
     */
    public SelectStatement where(Expression... expressions) {
        requireNonEmpty(expressions, "No expressions specified");
        this.expressions.addAll(asList(expressions));
        return this;
    }

    /**
     * Same as other method of same name, but only adds to the where clause expressions that are present.
     * @param maybeExpressions the expression that may be present or not
     * @return this object, for method chaining
     */
    @SafeVarargs
    public final SelectStatement where(Optional<Expression>... maybeExpressions) {
        requireNonEmpty(maybeExpressions, "No expressions specified");
        Arrays.stream(maybeExpressions).flatMap(Optionals::stream).forEach(this.expressions::add);
        return this;
    }

    /**
     * Adds one or more expressions to the where clause, if a condition is true.
     * @param expressionSuppliers the suppliers providing expressions to add
     * @return this object, for method chaining
     */
    @SafeVarargs
    public final SelectStatement whereIf(boolean condition, Supplier<Expression>... expressionSuppliers) {
        requireNonEmpty(expressionSuppliers, "No expression suppliers specified");
        if (condition) {
            Arrays.stream(expressionSuppliers).map(Supplier::get).forEach(this.expressions::add);
        }
        return this;
    }

    public SelectStatement groupBy(Field... groups) {
        requireNonEmpty(groups, "No groups specified");
        this.groups.addAll(asList(groups));
        return this;
    }

    public SelectStatement orderBy(Order... orders) {
        requireNonEmpty(orders, "No orders specified");
        this.orders.addAll(asList(orders));
        return this;
    }

    public SelectStatement limit(int limit) {
        this.limit = limit;
        return this;
    }

    public SelectStatement offset(int offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public String sql(Context context) {
        context.command(SELECT);
        validate();

        // Tables that are joined with should not be specified in the FROM clause
        Set<Table> joinedTables = joins.stream().map(Join::joined).collect(toSet());

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        if (distinct) {
            sb.append("distinct ");
        }

        sb.append(projections.stream()
                .map(p -> p.sql(context) + (isBlank(p.alias()) ? "" : " " + p.alias()))
                .collect(joining(", ")));

        sb.append(" from ");
        sb.append(tables.stream()
                .filter(not(joinedTables::contains))
                .map(t -> t.sql(context))
                .collect(joining(", ")));

        if (!joins.isEmpty()) {
            sb.append(" ");
            sb.append(joins.stream()
                    .map(j -> j.sql(context))
                    .collect(joining(" ")));
        }

        if (!expressions.isEmpty()) {
            sb.append(" where ");
            sb.append(expressions.stream()
                    .map(e -> e.sql(context))
                    .collect(joining(" and ")));
        }

        if (!groups.isEmpty()) {
            sb.append(" group by ");
            sb.append(groups.stream()
                    .map(g -> g.sql(context))
                    .collect(joining(", ")));
        }

        if (!orders.isEmpty()) {
            sb.append(" order by ");
            sb.append(orders.stream()
                    .map(o -> o.sql(context))
                    .collect(joining(", ")));
        }

        if (nonNull(offset) || nonNull(limit)) {
            if (context.getDialect().supports(LIMIT_OFFSET)) {
                if (nonNull(limit)) {
                    sb.append(" limit ?");
                }
                if (nonNull(offset)) {
                    sb.append(" offset ?");
                }
            } else {
                sb = addLimitOffsetFallback(context, sb, limit, offset);
            }
        }

        return sb.toString();
    }

    private StringBuilder addLimitOffsetFallback(Context context, StringBuilder innerSql, Integer limit, Integer offset) {
        String rowNum = context.getDialect().getRowNumLiteral().orElseThrow(
                () -> new RuntimeException("Dialect " + context.getDialect().getProductName() + " has no ROWNUM literal"));

        Integer fromRow = mapIfNonNull(offset, o -> o + 1);
        Integer toRow = mapIfNonNull(limit, l -> (offset != null ? offset : 0) + l);

        if (nonNull(fromRow) && nonNull(toRow)) {
            String limitSql = "select original.*, {ROWNUM} row_no from ( " + innerSql.toString() + " ) original where {ROWNUM} <= ?";
            String offsetSql = "select * from ( " + limitSql + " ) where row_no >= ?";
            return new StringBuilder(offsetSql.replace("{ROWNUM}", rowNum));
        }
        else if (nonNull(fromRow)) {
            String offsetSql = "select * from ( " + innerSql.toString() + " ) where {ROWNUM} >= ?";
            return new StringBuilder(offsetSql.replace("{ROWNUM}", rowNum));
        }
        else if (nonNull(toRow)) {
            String limitSql = "select * from ( " + innerSql.toString() + " ) where {ROWNUM} <= ?";
            return new StringBuilder(limitSql.replace("{ROWNUM}", rowNum));
        }

        return innerSql;
    }

    @Override
    public List<Object> params() {
        List<Object> params = expressions.stream().flatMap(Expression::params).collect(toList());
        if (nonNull(limit)) {
            params.add(limit);
        }
        if (nonNull(offset)) {
            params.add(offset);
        }
        return params;
    }

    private void validate() {
        if (tables.isEmpty()) {
            throw new IllegalStateException("No FROM clause specified");
        }

        List<Field> projectedFields = projections.stream().filter(instanceOf(Field.class)).map(castTo(Field.class)).collect(toList());
        validateFieldTableRelations(projectedFields.stream());

        List<Field> projectedFunctionFields = projections.stream().filter(instanceOf(FieldFunction.class)).map(castTo(FieldFunction.class)).map(FieldFunction::field).collect(toList());
        validateFieldTableRelations(projectedFunctionFields.stream());

        validateFieldTableRelations(joins.stream().flatMap(Join::fields));
        validateFieldTableRelations(expressions.stream().flatMap(Expression::fields));
        validateFieldTableRelations(groups.stream());
        validateFieldTableRelations(orders.stream().flatMap(Order::fields));
    }

    private void validateFieldTableRelations(Stream<Field> fields) {
        Set<String> tableNames = Stream.concat(tables.stream(), joins.stream().map(Join::joined))
                .map(Table::name).collect(toSet());
        fields
            .filter(f -> !tableNames.contains(f.table().name()))
            .findFirst()
            .ifPresent(f -> {
                throw new IllegalStateException("Field " + f.name() + " belongs to table " + f.table().name() + ", but is not specified in a FROM or JOIN clause");
            });
    }
}
