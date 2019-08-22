package no.vegvesen.vt.nvdb.commons.core.string;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public final class StringHelper {
    private StringHelper() {}

    public static boolean nonBlank(String string) {
        return nonNull(string) && !string.isEmpty();
    }

    public static boolean isBlank(String string) {
        return isNull(string) || string.isEmpty();
    }

    public static String toLower(String str) {
        return nonNull(str) ? str.toLowerCase() : null;
    }

    /**
     * Escapes "%" by replacing it with "%%". This is relevant for strings that will be used as input
     * to String.format, but should not be treated as a string with format specifiers.
     * @param input String that may contain one or more occurrences of "%".
     * @return New string with all occurrences of "%" replaced by "%%",
     */
    public static String escapePercentChars(String input) {
        return input.replace("%", "%%");
    }
}
