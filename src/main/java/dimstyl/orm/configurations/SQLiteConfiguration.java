package dimstyl.orm.configurations;

import dimstyl.orm.enums.DatabaseType;
import dimstyl.orm.exceptions.DatabaseConnectionException;
import dimstyl.orm.utils.PrintUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

enum SQLiteConfiguration implements DatabaseConfiguration {

    INSTANCE;

    private final DatabaseType DATABASE_TYPE = DatabaseType.SQLITE;
    private Connection connection = null;

    @Override
    public void connect(final String databaseName) throws DatabaseConnectionException {
        try {
            if (connection != null && !connection.isClosed()) return;

            // Ensure the directory exists
            Path dbDirectory = Paths.get("./db/sqlite");
            if (!Files.exists(dbDirectory)) Files.createDirectories(dbDirectory);

            final String connectionString = String.format("jdbc:sqlite:./db/sqlite/%s.db", databaseName);
            connection = connect(connectionString, DATABASE_TYPE.toString(), databaseName);
        } catch (SQLException | IOException e) {
            PrintUtils.print("\n⚠️ Could not connect to SQLite database '%s': %s.\n", databaseName, e.getMessage());
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
