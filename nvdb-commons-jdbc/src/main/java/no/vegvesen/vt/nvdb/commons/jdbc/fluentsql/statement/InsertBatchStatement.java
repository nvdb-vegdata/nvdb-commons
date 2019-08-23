package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.core.functional.Optionals;
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
import static no.vegvesen.vt.nvdb.commons.core.collection.CollectionHelper.streamIfNonNull;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonEmpty;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Command.INSERT;

public class InsertBatchStatement<T> extends PreparableStatement {
    private Table table;
    private List<FieldValueExtractor<? super T>> fieldValueExtractors;
    private Collection<? extends T> entities;

    InsertBatchStatement(Collection<? extends T> entities, Table table) {
        this.entities = requireNonEmpty(entities, "No entities specified");
        this.table = requireNonNull(table, "No table specified");
        this.fieldValueExtractors = new LinkedList<>();
    }

    public InsertBatchStatement<T> value(Field field, Function<? super T, Object> valueExtractor) {
        fieldValueExtractors.add(
                new FieldValueExtractor<>(
                        requireNonNull(field, "No field specified"),
                        requireNonNull(valueExtractor, "No valueExtractor specified")));
        return this;
    }

    @Override
    String sql(Context context) {
        context.command(INSERT);
        validate();

        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(table.sql(context));
        sb.append(" (");
        sb.append(fieldValueExtractors().map(fve -> fve.field().sql(context)).collect(joining(", ")));
        sb.append(") values ");
        sb.append(entities().map(e -> "("
                + fieldValueExtractors().map(fve -> fve.valueSql(context, e)).collect(joining(", "))
                + ")")
                .collect(joining(", ")));

        return sb.toString();
    }

    private Stream<? extends T> entities() {
        return streamIfNonNull(entities);
    }

    private Stream<FieldValueExtractor<? super T>> fieldValueExtractors() {
        return streamIfNonNull(fieldValueExtractors);
    }

    @Override
    List<Object> params() {
        return entities()
                .flatMap(e -> fieldValueExtractors()
                        .map(fve -> fve.param(e))
                        .flatMap(Optionals::stream))
                .collect(toList());
    }

    private void validate() {
        if (isNull(table)) {
            throw new IllegalStateException("No table specified");
        }
        if (fieldValueExtractors.isEmpty()) {
            throw new IllegalStateException("No values specified");
        }
        validateFieldTableRelations(fieldValueExtractors().map(FieldValueExtractor::field));
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
