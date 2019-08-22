package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.core.functional.Optionals;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Table;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Expression;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonEmpty;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Command.DELETE;

public class DeleteStatement extends PreparableStatement {
    private Table fromTable;
    private List<Expression> expressions;

    public DeleteStatement(Table table) {
        this.fromTable = requireNonNull(table, "No table specified");
        this.expressions = new LinkedList<>();
    }

    public DeleteStatement where(Expression... expressions) {
        requireNonEmpty(expressions, "No expressions specified");
        this.expressions.addAll(asList(expressions));
        return this;
    }

    /**
     * Same as other method of same name, but only adds to the where clause expressions that are present.
     * @param maybeExpressions the expressions that may be present or not
     * @return this object, for method chaining
     */
    @SafeVarargs
    public final DeleteStatement where(Optional<Expression>... maybeExpressions) {
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
    public final DeleteStatement whereIf(boolean condition, Supplier<Expression>... expressionSuppliers) {
        requireNonEmpty(expressionSuppliers, "No expression suppliers specified");
        if (condition) {
            Arrays.stream(expressionSuppliers).map(Supplier::get).forEach(this.expressions::add);
        }
        return this;
    }

    @Override
    String sql(Context context) {
        context.command(DELETE);
        validate();

        StringBuilder sb = new StringBuilder();
        sb.append("delete from ");
        sb.append(fromTable.sql(context));

        if (!expressions.isEmpty()) {
            sb.append(" where ");
            sb.append(expressions.stream()
                    .map(e -> e.sql(context))
                    .collect(joining(" and ")));
        }

        return sb.toString();
    }

    @Override
    List<Object> params() {
        return expressions.stream()
                .flatMap(Expression::params)
                .collect(toList());
    }

    private void validate() {
        if (isNull(fromTable)) {
            throw new IllegalStateException("No FROM clause specified");
        }

        // TODO: Verify that deleteTables is subset of fromTables

        validateFieldTableRelations(expressions.stream().flatMap(Expression::fields));
    }

    private void validateFieldTableRelations(Stream<Field> fields) {
        fields
            .filter(f -> !fromTable.name().equalsIgnoreCase(f.table().name()))
            .findFirst()
            .ifPresent(f -> {
                throw new IllegalStateException("Field " + f.name() + " belongs to table " + f.table().name() + ", but is not specified in the FROM clause");
            });
    }
}
