package dimstyl.orm.configurations;

import dimstyl.orm.enums.DatabaseType;
import dimstyl.orm.exceptions.DatabaseConnectionException;
import dimstyl.orm.utils.PrintUtils;

import java.sql.Connection;
import java.sql.SQLException;

enum H2Configuration implements DatabaseConfiguration {

    INSTANCE;

    private final DatabaseType DATABASE_TYPE = DatabaseType.H2;
    private Connection connection = null;

    @Override
    public void connect(final String databaseName) throws DatabaseConnectionException {
        try {
            if (connection != null && !connection.isClosed()) return;
            final String connectionString = "jdbc:h2:./db/h2/" + databaseName;
            connection = connect(connectionString, DATABASE_TYPE.toString(), databaseName);
        } catch (SQLException e) {
            PrintUtils.print("\n⚠️ Could not connect to H2 database '%s': %s.\n", databaseName, e.getMessage());
            throw new DatabaseConnectionException(e.getMessage(), e);
        }
    }

    @Override
    public void close() throws DatabaseConnectionException {
        try {
            connection = close(connection, DATABASE_TYPE.toString());
        } catch (SQLException e) {
            PrintUtils.print("⚠️ Could not close connection: %s\n", e.getMessage());
            throw new DatabaseConnectionException(e.getMessage(), e);
        }
    }

}
