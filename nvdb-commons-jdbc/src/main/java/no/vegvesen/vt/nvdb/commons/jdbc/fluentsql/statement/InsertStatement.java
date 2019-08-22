package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.core.functional.Optionals;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Table;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function.Function;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Command.INSERT;

public class InsertStatement extends PreparableStatement {
    private Table table;
    private List<FieldValue> fieldValues;

    InsertStatement(Table table) {
        this.table = requireNonNull(table, "No table specified");
        this.fieldValues = new LinkedList<>();
    }

    public InsertStatement value(Field field, Function function) {
        fieldValues.add(
                new FieldValue(
                        requireNonNull(field, "No field specified"),
                        requireNonNull(function, "No function specified")));
        return this;
    }

    public InsertStatement value(Field field, Object value) {
        fieldValues.add(
                new FieldValue(
                        requireNonNull(field, "No field specified"),
                        requireNonNull(value, "No value specified")));
        return this;
    }

    public InsertStatement value(Field field, Optional<?> maybeValue) {
        requireNonNull(field, "No field specified");
        requireNonNull(maybeValue, "No value specified");
        maybeValue.ifPresent(v -> fieldValues.add(new FieldValue(field, v)));
        return this;
    }

    @Override
    String sql(Context context) {
        context.command(INSERT);
        validate();

        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(table.sql(context));
        sb.append(" (");
        sb.append(fieldValues.stream().map(fv -> fv.field().sql(context)).collect(joining(", ")));
        sb.append(") values (").append(fieldValues.stream().map(fv -> fv.valueSql(context)).collect(joining(", ")));
        sb.append(")");

        return sb.toString();
    }

    @Override
    List<Object> params() {
        return fieldValues.stream().map(FieldValue::param).flatMap(Optionals::stream).collect(toList());
    }

    private void validate() {
        if (isNull(table)) {
            throw new IllegalStateException("No table specified");
        }
        if (fieldValues.isEmpty()) {
            throw new IllegalStateException("No values specified");
        }
        validateFieldTableRelations(fieldValues.stream().map(FieldValue::field));
    }

    private void validateFieldTableRelations(Stream<Field> fields) {
        fields
            .filter(f -> !table.name().equals(f.table().name()))
            .findFirst()
            .ifPresent(f -> {
                throw new IllegalStateException("Field " + f.name() + " belongs to table " + f.table().name() + ", not the table specified by the INTO clause");
            });
    }
}
