package no.vegvesen.vt.nvdb.commons.core.lang;

import java.util.Arrays;
import java.util.List;

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

    public static boolean isBlankOrWhitespace(String string) {
        if (!isBlank(string)) {
            for (int index = 0; index < string.length(); index++) {
                if (!Character.isWhitespace(string.charAt(index))) {
                    return false;
                }
            }
        }

        return true;
    }

    public static String toLower(String str) {
        return nonNull(str) ? str.toLowerCase() : null;
    }

    public static String toLowerFirstLetter(String str) {
        return nonNull(str) ? str.substring(0,1).toLowerCase() + str.substring(1) : null;
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

    public static List<String> split(String string, String regex) {
        return Arrays.asList(string.split(regex));
    }

    public static String generate(String s, int count) {
        StringBuilder b = new StringBuilder();
        for (int t = 0; t < count; t++) {
            b.append(s);
        }
        return b.toString();
    }

    public static String generate(String s, int count, String delimiter) {
        StringBuilder b = new StringBuilder();
        for (int t = 0; t < count; t++) {
            if (t != 0) {
                b.append(delimiter);
            }
            b.append(s);
        }
        return b.toString();
    }
}
