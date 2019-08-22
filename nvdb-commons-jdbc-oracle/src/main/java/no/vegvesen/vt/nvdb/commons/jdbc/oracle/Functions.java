package no.vegvesen.vt.nvdb.commons.jdbc.oracle;

public abstract class Functions {
    public static <T extends ObjectType> TreatValue<T> treat(ValueAs<T> valueAs) {
        return new TreatValue<>(valueAs);
    }

    public static <T extends ObjectType> ValueAs<T> value(ObjectTable table) {
        return new ValueAs<>(table);
    }
}
