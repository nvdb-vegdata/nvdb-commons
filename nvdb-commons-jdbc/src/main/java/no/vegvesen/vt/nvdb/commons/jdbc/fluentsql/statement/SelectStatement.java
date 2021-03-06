package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.core.functional.Optionals;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Join;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Table;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Expression;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.OptionalExpression;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function.FieldFunction;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function.aggregate.AggregateFunction;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.order.Order;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.projection.Projection;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.subquery.Subquery;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static no.vegvesen.vt.nvdb.commons.core.collection.CollectionHelper.isEmpty;
import static no.vegvesen.vt.nvdb.commons.core.collection.CollectionHelper.nonEmpty;
import static no.vegvesen.vt.nvdb.commons.core.collection.CollectionHelper.streamIfNonNull;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.require;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonEmpty;
import static no.vegvesen.vt.nvdb.commons.core.functional.Functions.castTo;
import static no.vegvesen.vt.nvdb.commons.core.functional.Optionals.mapIfNonNull;
import static no.vegvesen.vt.nvdb.commons.core.functional.Predicates.instanceOf;
import static no.vegvesen.vt.nvdb.commons.core.functional.Predicates.not;
import static no.vegvesen.vt.nvdb.commons.core.lang.StringHelper.isBlank;
import static no.vegvesen.vt.nvdb.commons.core.lang.StringHelper.nonBlank;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Command.SELECT;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect.Capability.LIMIT_OFFSET;

public class SelectStatement extends PreparableStatement {
    private List<Projection> projections;
    private List<Table> tables;
    private List<Join> joins;
    private Subquery subqueryFrom;
    private List<Expression> expressions;
    private List<Field> groups;
    private List<Order> orders;
    private boolean distinct;
    private Long limit;
    private Long offset;
    private boolean forUpdate;

    SelectStatement(boolean distinct, List<Projection> projections, List<Table> tables) {
        this.distinct = distinct;
        this.projections = requireNonEmpty(projections, "No projections specified");
        this.tables = requireNonEmpty(tables, "No tables specified");
        this.joins = new LinkedList<>();
        this.subqueryFrom = null;
        this.expressions = new LinkedList<>();
        this.groups = new LinkedList<>();
        this.orders = new LinkedList<>();
        this.forUpdate = false;
    }

    SelectStatement(boolean distinct, List<Projection> projections, Subquery subqueryFrom) {
        this.distinct = distinct;
        this.projections = requireNonEmpty(projections, "No projections specified");
        this.tables = null;
        this.joins = null;
        this.subqueryFrom = requireNonNull(subqueryFrom, "No subquery specified");
        this.expressions = new LinkedList<>();
        this.groups = new LinkedList<>();
        this.orders = new LinkedList<>();
        this.forUpdate = false;
    }

    public SelectStatement join(Join... joins) {
        requireNonEmpty(joins, "No joins specified");
        require(() -> isNull(subqueryFrom), "Can't combine a subquery 'from' clause with joins");
        this.joins.addAll(asList(joins));
        return this;
    }

    public SelectStatement leftOuterJoin(Join join) {
        requireNonNull(join, "No join specified");
        return join(join.leftOuter());
    }

    public SelectStatement rightOuterJoin(Join join) {
        requireNonNull(join, "No join specified");
        return join(join.rightOuter());
    }

    @SafeVarargs
    public final SelectStatement joinIf(boolean condition, Supplier<Join>... joinSuppliers) {
        requireNonEmpty(joins, "No join suppliers specified");
        require(() -> isNull(subqueryFrom), "Can't combine a subquery 'from' clause with joins");
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
    public final SelectStatement where(OptionalExpression... maybeExpressions) {
        requireNonEmpty(maybeExpressions, "No expressions specified");
        Arrays.stream(maybeExpressions).flatMap(OptionalExpression::stream).forEach(this.expressions::add);
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

    public SelectStatement limit(long limit) {
        this.limit = limit;
        return this;
    }

    public SelectStatement offset(long offset) {
        this.offset = offset;
        return this;
    }

    public SelectStatement forUpdate() {
        this.forUpdate = true;
        return this;
    }

    @Override
    public String sql(Context context) {
        final Context localContext = context.withCommand(SELECT);
        validate();

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        if (distinct) {
            sb.append("distinct ");
        }

        sb.append(projections.stream()
                .map(p -> p.sql(localContext) + (isBlank(p.alias()) ? "" : " " + p.alias()))
                .collect(joining(", ")));

        sb.append(" from ");

        if (nonNull(subqueryFrom)) {
            sb.append(subqueryFrom.sql(context));
            if (nonBlank(subqueryFrom.alias())) {
                sb.append(" ");
                sb.append(subqueryFrom.alias());
            }
        } else {
            // Tables that are joined with should not be specified in the FROM clause
            Set<Table> joinedTables = joins.stream().map(Join::joined).collect(toSet());

            sb.append(tables.stream()
                    .filter(not(joinedTables::contains))
                    .map(t -> t.sql(localContext))
                    .collect(joining(", ")));

            if (!joins.isEmpty()) {
                sb.append(" ");
                sb.append(joins.stream()
                        .map(j -> j.sql(localContext))
                        .collect(joining(" ")));
            }
        }

        if (!expressions.isEmpty()) {
            sb.append(" where ");
            sb.append(expressions.stream()
                    .map(e -> e.sql(localContext))
                    .collect(joining(" and ")));
        }

        if (!groups.isEmpty()) {
            sb.append(" group by ");
            sb.append(groups.stream()
                    .map(g -> g.sql(localContext))
                    .collect(joining(", ")));
        }

        if (!orders.isEmpty()) {
            sb.append(" order by ");
            sb.append(orders.stream()
                    .map(o -> o.sql(localContext))
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
                Long rowFrom = mapIfNonNull(offset, o -> o + 1);
                Long rowTo = mapIfNonNull(limit, l -> (nonNull(offset) ? offset : 0) + l);

                sb = addLimitOffsetFallback(context, sb, rowFrom, rowTo);

                offset = rowFrom;
                limit = rowTo;
            }
        }

        if (forUpdate) {
            sb.append(" for update");
        }

        return sb.toString();
    }

    private StringBuilder addLimitOffsetFallback(Context context, StringBuilder innerSql, Long rowFrom, Long rowTo) {
        String rowNum = context.getDialect().getRowNumLiteral().orElseThrow(
                () -> new RuntimeException("Dialect " + context.getDialect().getProductName() + " has no ROWNUM literal"));

        if (nonNull(rowFrom) && nonNull(rowTo)) {
            String limitSql = "select original.*, {ROWNUM} row_no from ( " + innerSql.toString() + " ) original where {ROWNUM} <= ?";
            String offsetSql = "select * from ( " + limitSql + " ) where row_no >= ?";
            return new StringBuilder(offsetSql.replace("{ROWNUM}", rowNum));
        }
        else if (nonNull(rowFrom)) {
            String offsetSql = "select * from ( " + innerSql.toString() + " ) where {ROWNUM} >= ?";
            return new StringBuilder(offsetSql.replace("{ROWNUM}", rowNum));
        }
        else if (nonNull(rowTo)) {
            String limitSql = "select * from ( " + innerSql.toString() + " ) where {ROWNUM} <= ?";
            return new StringBuilder(limitSql.replace("{ROWNUM}", rowNum));
        }

        return innerSql;
    }

    @Override
    public List<Object> params() {
        List<Object> params = new LinkedList<>();

        if (nonNull(subqueryFrom)) {
            params.addAll(subqueryFrom.params());
        }

        expressions.stream().flatMap(Expression::params).forEach(params::add);

        if (nonNull(limit)) {
            params.add(limit);
        }
        if (nonNull(offset)) {
            params.add(offset);
        }
        return params;
    }

    private void validate() {
        if (isEmpty(tables) && isNull(subqueryFrom)) {
            throw new IllegalStateException("No FROM clause specified");
        }

        List<Field> projectedFields = projections.stream()
                .filter(instanceOf(Field.class))
                .map(castTo(Field.class))
                .collect(toList());
        validateFieldTableRelations(projectedFields.stream());

        List<Field> projectedFunctionFields = projections.stream()
                .filter(instanceOf(FieldFunction.class))
                .map(castTo(FieldFunction.class))
                .map(FieldFunction::field)
                .flatMap(Optionals::stream)
                .collect(toList());
        validateFieldTableRelations(projectedFunctionFields.stream());

        if (nonNull(joins)) {
            validateFieldTableRelations(joins.stream().flatMap(Join::fields));
        }

        validateFieldTableRelations(expressions.stream().flatMap(Expression::fields));
        validateFieldTableRelations(groups.stream());
        validateFieldTableRelations(orders.stream().flatMap(Order::fields));

        if (forUpdate) {
            if (distinct || nonEmpty(groups) || projections.stream().anyMatch(instanceOf(AggregateFunction.class))) {
                throw new IllegalStateException("SELECT ... FOR UPDATE can't be used with DISTINCT, GROUP BY or aggregates");
            }
        }
    }

    private void validateFieldTableRelations(Stream<Field> fields) {
        Set<String> tableNames = Stream.concat(streamIfNonNull(tables), streamIfNonNull(joins).map(Join::joined))
                .map(Table::name).collect(toSet());
        fields
            .filter(f -> !tableNames.contains(f.table().name()))
            .findFirst()
            .ifPresent(f -> {
                throw new IllegalStateException("Field " + f.name() + " belongs to table " + f.table().name() + ", but is not specified in a FROM or JOIN clause");
            });
    }
}
