package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Table;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.projection.Projection;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static no.vegvesen.vt.nvdb.commons.core.collection.CollectionHelper.asNonEmptyList;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.constant.Constants.constant;

public class Statements {
    public static SelectFromBuilder select(long value) {
        return new SelectFromBuilder(false, singletonList(constant(value)));
    }

    public static SelectFromBuilder select(Projection firstProjection, Projection... moreProjections) {
        requireNonNull(firstProjection, "First projection is null");
        List<Projection> projections = asNonEmptyList(firstProjection, moreProjections);
        return new SelectFromBuilder(false, projections);
    }

    public static SelectFromBuilder selectDistinct(Projection firstProjection, Projection... moreProjections) {
        requireNonNull(firstProjection, "First projection is null");
        List<Projection> projections = asNonEmptyList(firstProjection, moreProjections);
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

    public static TruncateTableBuilder truncate() {
        return new TruncateTableBuilder();
    }

    public static TruncateStatement truncateTable(Table table) {
        return new TruncateStatement(table);
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
