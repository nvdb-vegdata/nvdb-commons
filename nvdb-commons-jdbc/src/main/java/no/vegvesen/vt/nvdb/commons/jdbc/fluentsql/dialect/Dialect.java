package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface Dialect {
    String getProductName();

    Optional<String> getRowNumLiteral();

    boolean supports(Capability capability);

    static Dialect fromConnection(Connection connection) {
        try {
            String productName = connection.getMetaData().getDatabaseProductName().toLowerCase();

            if (productName.contains("h2")) {
                return new H2Dialect();
            } else if (productName.contains("mysql")) {
                return new MySqlDialect();
            } else if (productName.contains("postgresql")) {
                return new PostgreSqlDialect();
            } else if (productName.contains("oracle")) {
                return new OracleDialect();
            } else {
                throw new UnsupportedOperationException("Database with product name " + productName + " not supported");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to detect SQL dialect from connection metadata", e);
        }
    }
}
