package no.vegvesen.vt.nvdb.commons.core.time;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static java.util.Objects.isNull;

public class DateTimeHelper {
    private DateTimeHelper() {}

    public static long epochMillis(int year, int month, int dayOfMonth) {
        return instant(year, month, dayOfMonth).toEpochMilli();
    }

    public static Instant instant(int year, int month, int dayOfMonth) {
        return toInstant(LocalDate.of(year, month, dayOfMonth));
    }

    public static Instant toInstant(LocalDate date) {
        return date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
    }

    public static boolean onOrBefore(LocalDate date, LocalDate endDate) {
        return isNull(endDate) || !date.isAfter(endDate);
    }

    public static boolean onOrAfter(LocalDate date, LocalDate startDate) {
        return !date.isBefore(startDate);
    }

    public static boolean afterOrEqualTo(LocalDate date, LocalDate otherDate) {
        return date.isEqual(otherDate) || date.isAfter(otherDate);
    }

    public static boolean beforeOrEqualTo(LocalDate date, LocalDate otherDate) {
        return date.isEqual(otherDate) || date.isBefore(otherDate);
    }

    public static String toIsoString(LocalDate date) {
        if (isNull(date)) {
            return "null";
        } else {
            return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }

    public static String toIsoString(LocalDateTime dateTime) {
        if (isNull(dateTime)) {
            return "null";
        } else {
            return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }
}
