package dimstyl.orm.configurations;

import dimstyl.orm.enums.DatabaseType;
import dimstyl.orm.exceptions.DatabaseConnectionException;
import dimstyl.orm.utils.ConsoleUtils;

import java.sql.Connection;
import java.sql.SQLException;

enum DerbyConfiguration implements DatabaseConfiguration {

    INSTANCE;

    private final DatabaseType DATABASE_TYPE = DatabaseType.DERBY;
    private Connection connection = null;

    @Override
    public void connect(String databaseName) throws DatabaseConnectionException {
        try {
            if (connection != null && !connection.isClosed()) return;
            final String connectionString = String.format("jdbc:derby:./db/derby/%s;create=true", databaseName);
            connection = connect(connectionString, DATABASE_TYPE.toString(), databaseName);
        } catch (SQLException e) {
            ConsoleUtils.printFormatted("\n⚠️ Could not connect to Derby database '%s': %s.\n", databaseName, e.getMessage());
            throw new DatabaseConnectionException(e.getMessage(), e);
        }
    }

    @Override
    public void close() throws DatabaseConnectionException {
        try {
            connection = close(connection, DATABASE_TYPE.toString());
        } catch (SQLException e) {
            ConsoleUtils.printFormatted("⚠️ Could not close connection: %s\n", e.getMessage());
            throw new DatabaseConnectionException(e.getMessage(), e);
        }
    }

}
