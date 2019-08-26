package no.vegvesen.vt.nvdb.commons.core.lang;

import java.time.LocalDate;

public class Comparators {
    public static <T extends Comparable<T>> T minimumOf(T a, T b) {
        return a == null ? b : (b == null ? a : (a.compareTo(b) < 0 ? a : b));
    }

    public static LocalDate earliestOf(LocalDate a, LocalDate b) {
        return a == null ? b : (b == null ? a : (a.compareTo(b) < 0 ? a : b));
    }

    public static <T extends Comparable<T>> T maximumOf(T a, T b) {
        return a == null ? b : (b == null ? a : (a.compareTo(b) > 0 ? a : b));
    }

    public static LocalDate maximumOf(LocalDate a, LocalDate b) {
        return a == null ? b : (b == null ? a : (a.compareTo(b) > 0 ? a : b));
    }
}
