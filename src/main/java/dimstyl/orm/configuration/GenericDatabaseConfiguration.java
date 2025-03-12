package dimstyl.orm.configuration;

import dimstyl.orm.enums.DatabaseType;
import dimstyl.orm.exceptions.DatabaseConnectionException;
import dimstyl.orm.utils.ConsoleUtils;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RequiredArgsConstructor
class GenericDatabaseConfiguration implements DatabaseConfiguration {

    private final DatabaseType databaseType;
    private Connection connection = null;

    @Override
    public void connect(String databaseName) throws DatabaseConnectionException {
        try {
            ConsoleUtils.printFormatted("\n🔄️ Connecting to %s database '%s'...\n", databaseType, databaseName);
            if (isConnected()) return;
            if (databaseType == DatabaseType.SQLITE) ensureDirectoryExists("./db/sqlite");
            final String connectionString = buildConnectionString(databaseName);
            connection = DriverManager.getConnection(connectionString);
            ConsoleUtils.printFormatted("✅ Connection established successfully\n");
        } catch (SQLException | IOException e) {
            ConsoleUtils.printFormatted("⚠️ Could not connect to %s database '%s'\n\tERROR: %s\n", databaseType, databaseName, e.getMessage());
            throw new DatabaseConnectionException(e.getMessage(), e);
        }
    }

    @Override
    public void close() throws DatabaseConnectionException {
        try {
            if (isConnected()) {
                ConsoleUtils.printFormatted("\n🔄️ Closing %s connection...\n", databaseType);
                connection.close();
                ConsoleUtils.printFormatted("✅ %s connection closed\n", databaseType);
                connection = null;
            }
        } catch (SQLException e) {
            ConsoleUtils.printFormatted("⚠️ Could not close connection\n\t%s\n", e.getMessage());
            throw new DatabaseConnectionException(e.getMessage(), e);
        }
    }

    @Override
    public Connection getConnection() throws DatabaseConnectionException {
        validateConnection();
        return connection;
    }

    private boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    private void validateConnection() throws DatabaseConnectionException {
        if (!isConnected())
            throw new DatabaseConnectionException("Database connection is either not established or has been closed.");
    }

    private String buildConnectionString(final String databaseName) {
        return switch (databaseType) {
            case DERBY -> String.format("jdbc:derby:./db/derby/%s;create=true", databaseName);
            case SQLITE -> String.format("jdbc:sqlite:./db/sqlite/%s.db", databaseName);
            case H2 -> "jdbc:h2:./db/h2/" + databaseName;
        };
    }

    private void ensureDirectoryExists(final String path) throws IOException {
        Path directory = Paths.get(path);
        if (!Files.exists(directory)) Files.createDirectories(directory);
    }

}
