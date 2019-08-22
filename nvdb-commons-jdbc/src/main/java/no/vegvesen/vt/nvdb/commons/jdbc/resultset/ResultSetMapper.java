package no.vegvesen.vt.nvdb.commons.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetMapper<Output> {
    Output map(ResultSet rs) throws SQLException;
}
