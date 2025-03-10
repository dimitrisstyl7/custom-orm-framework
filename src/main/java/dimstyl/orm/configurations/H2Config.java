package dimstyl.orm.configurations;

import dimstyl.orm.exceptions.DatabaseConnectionException;
import dimstyl.orm.utils.PrintUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class H2Config {

    private static Connection connection = null;

    private H2Config() {
    }

    public static void connect(final String databaseName) throws DatabaseConnectionException {
        try {
            if (connection != null && !connection.isClosed()) return;

            PrintUtils.print("\n🔄️ Connecting to database %s...\n", databaseName);
            connection = DriverManager.getConnection("jdbc:h2:./db/h2/" + databaseName);
            PrintUtils.print("✅ Connection to %s established successfully.\n", databaseName);
        } catch (SQLException e) {
            PrintUtils.print("⚠️ Could not connect to database: %s\n", e.getMessage());
            throw new DatabaseConnectionException(e.getMessage(), e);
        }
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                PrintUtils.print("\n🔄️ Closing connection...\n");
                connection.close();
                connection = null;
                PrintUtils.print("✅ Database connection closed.\n");
            }
        } catch (SQLException e) {
            PrintUtils.print("⚠️ Could not close connection: %s\n", e.getMessage());
        }
    }

}
