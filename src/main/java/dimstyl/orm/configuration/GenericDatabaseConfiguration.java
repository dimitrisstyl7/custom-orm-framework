package dimstyl.orm.configuration;

import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.enums.SqlOperation;
import dimstyl.orm.exceptions.DatabaseConnectionException;
import dimstyl.orm.internal.utils.ConsoleUtils;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

enum GenericDatabaseConfiguration implements DatabaseConfiguration {

    INSTANCE;

    @Setter
    private DatabaseEngine databaseEngine;
    private Connection connection = null;

    @Override
    public void connect(final String databaseName, final SqlOperation sqlOperation) throws DatabaseConnectionException {
        try {
            ConsoleUtils.printFormatted("\n🔄️ Connecting to %s database '%s'...\n", databaseEngine, databaseName);
            if (isConnected()) return;
            if (databaseEngine == DatabaseEngine.SQLITE) ensureDirectoryExists("./db/sqlite");
            final String connectionString = buildConnectionString(databaseName, sqlOperation);
            connection = DriverManager.getConnection(connectionString);
            ConsoleUtils.printFormatted("✅ Connection established successfully\n");
        } catch (SQLException | IOException e) {
            ConsoleUtils.printFormatted("⚠️ Could not connect to %s database '%s'\n\tERROR: %s\n", databaseEngine, databaseName, e.getMessage());
            throw new DatabaseConnectionException(e.getMessage(), e);
        }
    }

    @Override
    public void close() throws DatabaseConnectionException {
        try {
            if (isConnected()) {
                ConsoleUtils.printFormatted("\n🔄️ Closing %s connection...\n", databaseEngine);
                connection.close();
                ConsoleUtils.printFormatted("✅ %s connection closed\n", databaseEngine);
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

    private void validateConnection() throws DatabaseConnectionException {
        if (!isConnected())
            throw new DatabaseConnectionException("Database connection is either not established or has been closed.");
    }

    private boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    private String buildConnectionString(final String databaseName, final SqlOperation sqlOperation) {
        String projectRoot = Paths.get("").toAbsolutePath().toString();
        return switch (databaseEngine) {
            case DERBY -> String.format(
                    "jdbc:derby:%s/db/derby/%s.db%s",
                    projectRoot,
                    databaseName,
                    sqlOperation == SqlOperation.CREATE_TABLE ? ";create=true" : ""
            );
            case SQLITE -> String.format("jdbc:sqlite:%s/db/sqlite/%s.db", projectRoot, databaseName);
            case H2 -> String.format("jdbc:h2:%s/db/h2/%s", projectRoot, databaseName);
        };
    }

    private void ensureDirectoryExists(final String path) throws IOException {
        Path directory = Paths.get(path);
        if (!Files.exists(directory)) Files.createDirectories(directory);
    }

}
