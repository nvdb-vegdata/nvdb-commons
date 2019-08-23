package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function.system;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.projection.Projection;

public class CurrentTimestamp implements SystemFunction {
    private String alias;

    @Override
    public Projection as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String sql(Context context) {
        return "current_timestamp";
    }
}
