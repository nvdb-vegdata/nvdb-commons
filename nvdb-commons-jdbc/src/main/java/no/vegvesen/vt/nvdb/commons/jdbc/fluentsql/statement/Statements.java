package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Table;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.projection.Projection;

import java.util.Collection;

import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.constant.Constants.constant;

public class Statements {
    public static SelectFromBuilder select(long value) {
        return new SelectFromBuilder(false, constant(value));
    }

    public static SelectFromBuilder select(Projection... projections) {
        return new SelectFromBuilder(false, projections);
    }

    public static SelectFromBuilder selectDistinct(Projection... projections) {
        return new SelectFromBuilder(true, projections);
    }

    public static UpdateStatement update(Table table) {
        return new UpdateStatement(table);
    }

    public static DeleteFromBuilder delete() {
        return new DeleteFromBuilder();
    }

    public static DeleteStatement deleteFrom(Table table) {
        return new DeleteStatement(table);
    }

    public static InsertIntoBuilder insert() {
        return new InsertIntoBuilder();
    }

    public static InsertStatement insertInto(Table table) {
        return new InsertStatement(table);
    }

    public static <T> InsertBatchIntoBuilder<T> insertBatch(Collection<T> entities) {
        return new InsertBatchIntoBuilder<>(entities);
    }
}
