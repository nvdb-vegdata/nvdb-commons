package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.constant;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;

import static java.util.Objects.requireNonNull;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonBlank;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Command.SELECT;

public class NullConstant implements Constant {
    private Field replacedField;
    private String alias;

    @Override
    public NullConstant as(String alias) {
        this.alias = requireNonBlank(alias, "No alias specified");
        return this;
    }

    @Override
    public NullConstant forField(Field field) {
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
        if (context.isCommand(SELECT)) {
            return "null";
        } else {
            throw new IllegalStateException("The Null constant can only be used in a SELECT statement");
        }
    }
}
