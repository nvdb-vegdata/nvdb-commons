package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.subquery;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Sql;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement.SelectStatement;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonBlank;

public class Subquery implements Sql {
    private final SelectStatement selectStatement;
    private final String alias;

    public Subquery(SelectStatement selectStatement) {
        this.selectStatement = requireNonNull(selectStatement, "No select statement specified");
        this.alias = null;
    }

    private Subquery(SelectStatement selectStatement, String alias) {
        this.selectStatement = requireNonNull(selectStatement, "No select statement specified");
        this.alias = requireNonBlank(alias, "No alias specified");
    }

    public Subquery as(String alias) {
        return new Subquery(selectStatement, alias);
    }

    public String alias() {
        return alias;
    }

    public List<Object> params() {
        return selectStatement.params();
    }

    @Override
    public String sql(Context context) {
        return "(" + selectStatement.sql(context) + ")";
    }
}
