package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function.system;

public class SystemFunctions {
    public static CurrentTimestamp currentTimestamp() {
        return new CurrentTimestamp();
    }
}
