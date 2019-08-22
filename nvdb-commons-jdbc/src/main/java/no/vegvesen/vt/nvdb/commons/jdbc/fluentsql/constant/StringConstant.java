package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.constant;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonBlank;

public class StringConstant implements Constant {
    private final String value;
    private Field replacedField;
    private String alias;

    public StringConstant(String value) {
        this.value = value;
    }

    @Override
    public StringConstant as(String alias) {
        this.alias = requireNonBlank(alias, "No alias specified");
        return this;
    }

    @Override
    public StringConstant forField(Field field) {
        this.replacedField = requireNonNull(field, "No field specified");
        this.alias = field.alias();
        return this;
    }

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String sql(Context context) {
        return isNull(value) ? "null" : "'" + value + "'";
    }
}
