package no.vegvesen.vt.nvdb.commons.jdbc.resultset;

import java.sql.SQLException;
import java.util.function.Function;

/**
 * Maps a single entry (row) from a JDBC ResultSet
 */
@FunctionalInterface
public interface ResultSetRowMapper<Output> {
    Output map(ResultSetInspector rsi) throws SQLException;

    default <NewOutput> ResultSetRowMapper<NewOutput> andThen(Function<Output, NewOutput> after) {
        return rsi -> after.apply(map(rsi));
    }
}
