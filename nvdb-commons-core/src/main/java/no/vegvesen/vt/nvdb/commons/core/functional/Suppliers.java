package no.vegvesen.vt.nvdb.commons.core.functional;

import java.util.function.Supplier;

/**
 * General purpose suppliers.
 */
public final class Suppliers {
    private Suppliers() {}

    public static Supplier<RuntimeException> illegalArgumentException(String msg, Object... args) {
        return () -> new IllegalArgumentException(String.format(msg, args));
    }

    public static Supplier<RuntimeException> illegalStateException(String msg, Object... args) {
        return () -> new IllegalStateException(String.format(msg, args));
    }

    public static Supplier<RuntimeException> runtimeException(String msg, Object... args) {
        return () -> new RuntimeException(String.format(msg, args));
    }
}
