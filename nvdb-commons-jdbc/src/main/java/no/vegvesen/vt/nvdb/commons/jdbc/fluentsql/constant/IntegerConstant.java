package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.constant;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonBlank;

public class IntegerConstant implements Constant {
    private final Long value;
    private Field replacedField;
    private String alias;

    public IntegerConstant(Long value) {
        this.value = value;
    }

    @Override
    public IntegerConstant as(String alias) {
        this.alias = requireNonBlank(alias, "No alias specified");
        return this;
    }

    @Override
    public IntegerConstant forField(Field field) {
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
        return isNull(value) ? "null" : Long.toString(value);
    }
}
