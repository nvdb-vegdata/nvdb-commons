package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql;

public class Tables {
    public static Table table(String name) {
        return new Table(name);
    }
}
