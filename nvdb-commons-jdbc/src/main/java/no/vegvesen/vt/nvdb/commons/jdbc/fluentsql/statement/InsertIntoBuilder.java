package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Table;

import static java.util.Objects.requireNonNull;

public class InsertIntoBuilder {
    public InsertStatement into(Table table) {
        return new InsertStatement(requireNonNull(table, "No table specified"));
    }
}
