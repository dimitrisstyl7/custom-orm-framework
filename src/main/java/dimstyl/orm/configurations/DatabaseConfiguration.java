package dimstyl.orm.configurations;

import dimstyl.orm.exceptions.DatabaseConnectionException;
import dimstyl.orm.utils.ConsoleUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface DatabaseConfiguration extends AutoCloseable {

    void connect(final String databaseName) throws DatabaseConnectionException;

    @Override
    void close() throws DatabaseConnectionException;

    default Connection connect(final String connectionString, final String databaseType, final String databaseName)
            throws SQLException {
        ConsoleUtils.printFormatted("\n🔄️ Connecting to %s database '%s'...\n", databaseType, databaseName);
        final Connection connection = DriverManager.getConnection(connectionString);
        ConsoleUtils.printFormatted("✅ Successfully connected to %s database '%s'\n", databaseType, databaseName);
        return connection;
    }

    default Connection close(final Connection connection, final String databaseType)
            throws SQLException {
        if (connection != null && !connection.isClosed()) {
            ConsoleUtils.printFormatted("\n🔄️ Closing %s connection...\n", databaseType);
            connection.close();
            ConsoleUtils.printFormatted("✅ %s connection closed\n", databaseType);
            return null;
        }
        return connection;
    }

}
