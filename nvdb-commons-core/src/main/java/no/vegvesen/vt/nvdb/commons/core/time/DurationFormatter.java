package no.vegvesen.vt.nvdb.commons.core.time;

import java.time.Duration;

public class DurationFormatter {
    public static String format(Duration duration) {
        long millis = duration.toMillis();
        if (millis < 1000) {
            return millis + " ms";
        }

        long seconds = millis / 1000;
        millis = millis % 1000;
        if (seconds < 60) {
            return String.format("%d.%03d secs", seconds, millis);
        }

        long minutes = seconds / 60;
        seconds = seconds % 60;
        if (minutes < 60) {
            return String.format("%d:%02d mins", minutes, seconds);
        }

        long hours = minutes / 60;
        minutes = minutes % 60;
        return String.format("%d:%02d:%02d hours", hours, minutes, seconds);
    }
}
