package no.vegvesen.vt.nvdb.commons.jdbc.resultset;

import java.time.LocalDate;

public class TimestampHelper {
    private TimestampHelper() {}

    public static LocalDate toLocalDate(java.sql.Timestamp timestamp) {
        return timestamp.toLocalDateTime().toLocalDate();
    }
}
