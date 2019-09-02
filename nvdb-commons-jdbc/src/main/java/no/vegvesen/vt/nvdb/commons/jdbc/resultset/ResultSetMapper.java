package no.vegvesen.vt.nvdb.commons.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

/**
 * Maps all rows from a JDBC ResultSet
 */
@FunctionalInterface
public interface ResultSetMapper<Output> {
    Output map(ResultSet rs) throws SQLException;

    default <NewOutput> ResultSetMapper<NewOutput> andThen(Function<Output, NewOutput> after) {
        return rs -> after.apply(map(rs));
    }
}
