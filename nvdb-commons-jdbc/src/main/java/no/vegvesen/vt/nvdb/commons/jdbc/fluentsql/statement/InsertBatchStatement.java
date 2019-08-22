package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Table;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonEmpty;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Command.INSERT;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement.Helpers.paramMarkers;

public class InsertBatchStatement<T> extends PreparableStatement {
    private Table table;
    private List<Field> fields;
    private List<Function<? super T, Object>> valueExtractors;
    private Collection<? extends T> entities;

    InsertBatchStatement(Collection<? extends T> entities, Table table) {
        this.entities = requireNonEmpty(entities, "No entities specified");
        this.table = requireNonNull(table, "No table specified");
        this.fields = new LinkedList<>();
        this.valueExtractors = new LinkedList<>();
    }

    public InsertBatchStatement<T> value(Field field, Function<? super T, Object> valueExtractor) {
        fields.add(requireNonNull(field, "No field specified"));
        valueExtractors.add(requireNonNull(valueExtractor, "No valueExtractor specified"));
        return this;
    }

    @Override
    String sql(Context context) {
        context.command(INSERT);
        validate();

        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(table.sql(context));
        sb.append(" (");
        sb.append(fields.stream().map(f -> f.sql(context)).collect(joining(", ")));
        sb.append(") values ");
        sb.append(Stream.generate(() -> "(" + paramMarkers(fields.size()) + ")")
                .limit(entities.size())
                .collect(joining(", ")));

        return sb.toString();
    }

    @Override
    List<Object> params() {
        return entities.stream()
                .flatMap(e -> valueExtractors.stream()
                        .map(ve -> ve.andThen(Helpers::unwrapOptional))
                        .map(ve -> ve.apply(e)))
                .collect(toList());
    }

    private void validate() {
        if (isNull(table)) {
            throw new IllegalStateException("No table specified");
        }
        if (fields.isEmpty()) {
            throw new IllegalStateException("No values specified");
        }
        validateFieldTableRelations(fields.stream());
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
