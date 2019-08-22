package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Table;

import java.util.Collection;

import static java.util.Objects.requireNonNull;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonEmpty;

public class InsertBatchIntoBuilder<T> {
    private Collection<? extends T> entities;

    InsertBatchIntoBuilder(Collection<? extends T> entities) {
        this.entities = requireNonEmpty(entities, "No entities specified");
    }

    public InsertBatchStatement<T> into(Table table) {
        return new InsertBatchStatement<>(entities, requireNonNull(table, "No table specified"));
    }
}
