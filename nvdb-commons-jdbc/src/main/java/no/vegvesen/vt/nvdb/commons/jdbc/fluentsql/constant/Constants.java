package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.constant;

import java.util.UUID;

import static no.vegvesen.vt.nvdb.commons.core.functional.Optionals.mapIfNonNull;

public abstract class Constants {
    public static StringConstant constant(UUID value) {
        return new StringConstant(mapIfNonNull(value, UUID::toString));
    }

    public static StringConstant constant(Enum<? extends Enum<?>> value) {
        return new StringConstant(mapIfNonNull(value, Enum::name));
    }

    public static StringConstant constant(String value) {
        return new StringConstant(value);
    }

    public static IntegerConstant constant(Long value) {
        return new IntegerConstant(value);
    }

    public static NullConstant nullValue() {
        return new NullConstant();
    }
}
