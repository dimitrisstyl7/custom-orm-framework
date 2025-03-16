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

/**
 * Singleton implementation of {@link DatabaseConfiguration} for managing database connections.
 * <p>
 * This enum provides methods for establishing, retrieving, and closing database connections
 * while supporting multiple database engines such as Derby, SQLite, and H2.
 * </p>
 */
enum GenericDatabaseConfiguration implements DatabaseConfiguration {

    /**
     * The singleton instance of the database configuration.
     */
    INSTANCE;

    /**
     * The database engine used for the connection.
     */
    @Setter
    private DatabaseEngine databaseEngine;

    /**
     * The active database connection.
     */
    private Connection connection = null;

    /**
     * Establishes a connection to the specified database.
     * <p>
     * If the database engine is SQLite, it ensures that the required directory exists.
     * The connection string is built based on the selected {@link DatabaseEngine} and the SQL operation.
     * </p>
     *
     * @param databaseName The name of the database.
     * @param sqlOperation The type of {@link SqlOperation} to perform.
     * @throws DatabaseConnectionException If the connection cannot be established.
     */
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

    /**
     * Closes the active database connection if it exists.
     * <p>
     * If the connection is active, it is closed, and resources are released.
     * </p>
     *
     * @throws DatabaseConnectionException If an error occurs while closing the connection.
     */
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

    /**
     * Retrieves the active database connection.
     * <p>
     * If the connection is not established or has been closed, an exception is thrown.
     * </p>
     *
     * @return The active {@link Connection} instance.
     * @throws DatabaseConnectionException If no active connection is available.
     */
    @Override
    public Connection getConnection() throws DatabaseConnectionException {
        validateConnection();
        return connection;
    }

    /**
     * Validates whether a connection is active.
     * <p>
     * If the connection is not established or has been closed, an exception is thrown.
     * </p>
     *
     * @throws DatabaseConnectionException If the connection is not available.
     */
    private void validateConnection() throws DatabaseConnectionException {
        if (!isConnected())
            throw new DatabaseConnectionException("Database connection is either not established or has been closed.");
    }

    /**
     * Checks if there is an active database connection.
     *
     * @return {@code true} if a valid connection exists, {@code false} otherwise.
     */
    private boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Builds the connection string based on the selected {@link DatabaseEngine} and the {@link SqlOperation}.
     * <p>
     *     <ul>
     *         <li>For Derby: The database is stored in the project's {@code /db/derby} directory.</li>
     *         <li>For SQLite: The database is stored in the project's {@code /db/sqlite} directory.</li>
     *         <li>For H2: The database is stored in the project's {@code /db/h2} directory.</li>
     *     </ul>
     * </p>
     *
     * @param databaseName The name of the database.
     * @param sqlOperation The {@link SqlOperation} to determine if database creation is needed.
     * @return A connection string for the specified database engine.
     */
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

    /**
     * Ensures that the specified directory exists.
     * <p>
     * If the directory does not exist, it is created.
     * </p>
     *
     * @param path The directory path to check or create.
     * @throws IOException If an error occurs while creating the directory.
     */
    private void ensureDirectoryExists(final String path) throws IOException {
        Path directory = Paths.get(path);
        if (!Files.exists(directory)) Files.createDirectories(directory);
    }

}
