package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect;

public class H2CustomFunctions {
    public static Long toNumber(String value, String format) {
        return value == null ? null : Long.valueOf(value);
    }
}
