package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Table;

import static java.util.Objects.requireNonNull;

public class TruncateTableBuilder {
    public TruncateStatement table(Table table) {
        return new TruncateStatement(requireNonNull(table, "No table specified"));
    }
}
