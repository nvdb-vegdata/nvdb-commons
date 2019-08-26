package no.vegvesen.vt.nvdb.commons.core.contract;

import no.vegvesen.vt.nvdb.commons.core.collection.ArrayHelper;
import no.vegvesen.vt.nvdb.commons.core.collection.CollectionHelper;

import java.util.Collection;

import static java.util.Objects.requireNonNull;
import static no.vegvesen.vt.nvdb.commons.core.lang.StringHelper.nonBlank;

public final class Requires {

    private Requires() {}

    public static int[] requireNonEmpty(int[] array, String msg, Object... args) {
        requireNonNull(array, String.format(msg, args));
        require(() -> ArrayHelper.nonEmpty(array), msg, args);
        return array;
    }

    public static long[] requireNonEmpty(long[] array, String msg, Object... args) {
        requireNonNull(array, String.format(msg, args));
        require(() -> ArrayHelper.nonEmpty(array), msg, args);
        return array;
    }

    public static <T> T[] requireNonEmpty(T[] array, String msg, Object... args) {
        requireNonNull(array, String.format(msg, args));
        require(() -> ArrayHelper.nonEmpty(array), msg, args);
        return array;
    }

    public static <T extends Collection<?>> T requireNonEmpty(T collection, String msg, Object... args) {
        requireNonNull(collection, String.format(msg, args));
        require(() -> CollectionHelper.nonEmpty(collection), msg, args);
        return collection;
    }

    public static String requireNonBlank(String string, String msg, Object... args) {
        requireNonNull(string, String.format(msg, args));
        require(() -> nonBlank(string), msg, args);
        return string;
    }

    public static void require(Requirement requirement, String msg, Object... args) {
        if (!requirement.test()) {
            throw new IllegalArgumentException(String.format(msg, args));
        }
    }

    public static void precondition(Requirement requirement, String msg, Object... args) {
        if (!requirement.test()) {
            throw new IllegalStateException(String.format(msg, args));
        }
    }
}
