package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function.aggregate;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;

public abstract class Aggregates {
    public static Max max(Field field) {
        return new Max(field);
    }

    public static Min min(Field field) {
        return new Min(field);
    }

    public static Count count(Field field) {
        return new Count(field);
    }
}
