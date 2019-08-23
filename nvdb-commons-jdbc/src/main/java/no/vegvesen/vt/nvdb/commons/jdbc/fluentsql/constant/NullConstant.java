package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.constant;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.projection.Projection;

import static java.util.Objects.requireNonNull;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonBlank;

public class NullConstant implements Constant {
    private String alias;

    @Override
    public Object value() {
        return null;
    }

    @Override
    public Projection as(String alias) {
        this.alias = requireNonBlank(alias, "No alias specified");
        return this;
    }

    @Override
    public Projection forField(Field field) {
        requireNonNull(field, "No field specified");
        this.alias = field.alias();
        return this;
    }

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String sql(Context context) {
        return "null";
    }
}
