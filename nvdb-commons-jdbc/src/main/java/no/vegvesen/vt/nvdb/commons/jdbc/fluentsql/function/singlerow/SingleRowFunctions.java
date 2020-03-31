package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function.singlerow;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;

public abstract class SingleRowFunctions {
    public static Upper upper(Field field) {
        return new Upper(field);
    }

    public static Lower lower(Field field) {
        return new Lower(field);
    }

    public static ToNumber to_number(Field field, int precision, int scale) {
        return new ToNumber(field, precision, scale);
    }

    public static ToNumber to_number(Field field, int precision) {
        return new ToNumber(field, precision, 0);
    }
}
