package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function.Function;

import java.util.Optional;

class FieldValue {
    private final Field field;
    private final Object value;

    FieldValue(Field field, Object value) {
        this.field = field;
        this.value = value;
    }

    Field field() {
        return field;
    }

    Optional<Object> param() {
        return isParameterized() ? Optional.ofNullable(value) : Optional.empty();
    }

    String valueSql(Context context) {
        if (isParameterized()) {
            return "?";
        } else {
            Function function = (Function)value;
            return function.sql(context);
        }
    }

    private boolean isParameterized() {
        return !(value instanceof Function);
    }
}