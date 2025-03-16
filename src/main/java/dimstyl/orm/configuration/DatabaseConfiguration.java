package dimstyl.orm.configuration;

import dimstyl.orm.enums.SqlOperation;
import dimstyl.orm.exceptions.DatabaseConnectionException;

import java.sql.Connection;

/**
 * Represents a configuration interface for managing database connections.
 * <p>
 * This interface provides methods to establish, retrieve, and close a database connection
 * while handling different SQL operations.
 * </p>
 */
public interface DatabaseConfiguration extends AutoCloseable {

    /**
     * Establishes a connection to the specified database.
     * <p>
     * This method connects to the database using the provided database name and SQL operation type.
     * If the connection fails, a {@link DatabaseConnectionException} is thrown.
     * </p>
     *
     * @param databaseName The name of the database to connect to.
     * @param sqlOperation The type of {@link SqlOperation} to perform.
     * @throws DatabaseConnectionException If an error occurs while connecting to the database.
     */
    void connect(final String databaseName, final SqlOperation sqlOperation) throws DatabaseConnectionException;

    /**
     * Closes the active database connection.
     * <p>
     * This method ensures that all resources associated with the database connection are released.
     * If an error occurs during closing, a {@link DatabaseConnectionException} is thrown.
     * </p>
     *
     * @throws DatabaseConnectionException If an error occurs while closing the connection.
     */
    @Override
    void close() throws DatabaseConnectionException;

    /**
     * Retrieves the active database connection.
     * <p>
     * If the connection is not established, this method should throw a {@link DatabaseConnectionException}.
     * </p>
     *
     * @return The active {@link Connection} instance.
     * @throws DatabaseConnectionException If no connection is available or an error occurs while retrieving it.
     */
    Connection getConnection() throws DatabaseConnectionException;

}
