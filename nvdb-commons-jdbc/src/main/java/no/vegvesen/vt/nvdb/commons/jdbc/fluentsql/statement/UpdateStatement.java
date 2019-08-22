package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.core.functional.Optionals;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Table;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Expression;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function.Function;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonEmpty;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Command.UPDATE;

public class UpdateStatement extends PreparableStatement {
    private Table table;
    private List<FieldValue> fieldValues;
    private List<Expression> expressions;

    UpdateStatement(Table table) {
        this.table = requireNonNull(table, "No table specified");
        this.fieldValues = new LinkedList<>();
        this.expressions = new LinkedList<>();
    }

    public UpdateStatement set(Field field, Function function) {
        fieldValues.add(
                new FieldValue(
                        requireNonNull(field, "No field specified"),
                        requireNonNull(function, "No function specified")));
        return this;
    }

    public UpdateStatement set(Field field, Object value) {
        fieldValues.add(
                new FieldValue(
                        requireNonNull(field, "No field specified"),
                        requireNonNull(value, "No value specified")));
        return this;
    }

    public UpdateStatement set(Field field, Optional<?> maybeValue) {
        requireNonNull(field, "No field specified");
        requireNonNull(maybeValue, "No value specified");
        maybeValue.ifPresent(v -> fieldValues.add(new FieldValue(field, v)));
        return this;
    }

    public UpdateStatement where(Expression... expressions) {
        requireNonEmpty(expressions, "No expressions specified");
        this.expressions.addAll(Arrays.asList(expressions));
        return this;
    }

    /**
     * Same as other method of same name, but only adds to the where clause expressions that are present.
     * @param maybeExpressions the expressions that may be present or not
     * @return this object, for method chaining
     */
    @SafeVarargs
    public final UpdateStatement where(Optional<Expression>... maybeExpressions) {
        requireNonEmpty(maybeExpressions, "No expressions specified");
        Arrays.stream(maybeExpressions).flatMap(Optionals::stream).forEach(this.expressions::add);
        return this;
    }

    @Override
    String sql(Context context) {
        context.command(UPDATE);
        validate();

        StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(table.sql(context));
        sb.append(" set ");
        sb.append(fieldValues.stream().map(fv -> fv.field().sql(context) + " = " + fv.valueSql(context)).collect(joining(", ")));
        if (!expressions.isEmpty()) {
            sb.append(" where ");
            sb.append(expressions.stream().map(e -> e.sql(context)).collect(joining(" and ")));
        }

        return sb.toString();
    }

    @Override
    List<Object> params() {
        return Stream.concat(fieldValues.stream().map(FieldValue::param).flatMap(Optionals::stream), expressions.stream().flatMap(Expression::params)).collect(toList());
    }

    private void validate() {
        if (fieldValues.isEmpty()) {
            throw new IllegalStateException("No values to set");
        }
        validateFieldTableRelations(fieldValues.stream().map(FieldValue::field));
        validateFieldTableRelations(expressions.stream().flatMap(Expression::fields));
    }

    private void validateFieldTableRelations(Stream<Field> fields) {
        fields
            .filter(f -> !table.name().equalsIgnoreCase(f.table().name()))
            .findFirst()
            .ifPresent(f -> {
                throw new IllegalStateException("Field " + f.name() + " belongs to table " + f.table().name() + ", but is not specified in the UPDATE clause");
            });
    }
}
