package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Table;

import static java.util.Objects.requireNonNull;

public class DeleteFromBuilder {
    public DeleteStatement from(Table table) {
        return new DeleteStatement(requireNonNull(table, "No table specified"));
    }
}
