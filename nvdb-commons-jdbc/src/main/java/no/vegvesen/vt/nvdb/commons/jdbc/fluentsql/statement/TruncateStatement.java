package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Table;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Command.TRUNCATE;

public class TruncateStatement extends PreparableStatement {
    private Table table;

    TruncateStatement(Table table) {
        this.table = requireNonNull(table, "No table specified");
    }

    @Override
    String sql(Context context) {
        context.command(TRUNCATE);

        StringBuilder sb = new StringBuilder();
        sb.append("truncate table ");
        sb.append(table.sql(context));

        return sb.toString();
    }

    @Override
    List<Object> params() {
        return emptyList();
    }
}
