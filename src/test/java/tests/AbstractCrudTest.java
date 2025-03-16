package tests;

import dimstyl.orm.configuration.DatabaseConfigurationFactory;
import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.enums.SqlOperation;
import dimstyl.orm.internal.processors.RepositoryProcessor;
import dimstyl.orm.internal.sql.execution.SqlQueryExecutor;
import dimstyl.orm.internal.sql.generator.SqlQueryGenerator;
import dimstyl.orm.internal.sql.generator.SqlQueryGeneratorFactory;
import dimstyl.orm.internal.utils.ConsoleUtils;
import dimstyl.orm.metadata.RepositoryMetadata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Abstract test class for CRUD (Create, Read, Update, Delete) operations using ORM-based repository processing.
 * <p>
 * This class provides utility methods for seeding and deleting test data,
 * retrieving all records from a repository, and deleting records by ID.
 * </p>
 * <p><strong>Note:</strong> This class only supports read and delete operations.
 * Create and update operations are not implemented.</p>
 *
 * @see RepositoryProcessor
 * @see SqlQueryGenerator
 * @see SqlQueryExecutor
 * @see DatabaseConfigurationFactory
 */
abstract class AbstractCrudTest extends AbstractTest {

    /**
     * Seeds the database with test data using an SQL script.
     *
     * @param databaseName   The name of the database.
     * @param databaseEngine The {@link DatabaseEngine} to be used.
     * @throws IOException  If an error occurs while reading the SQL script file.
     * @throws SQLException If an error occurs while executing SQL queries.
     */
    static void seedData(final String databaseName, final DatabaseEngine databaseEngine)
            throws IOException, SQLException {
        final Path path = Paths.get("src/test/resources/seed_data.sql");
        try (final Stream<String> stream = Files.lines(path)) {
            final List<String> queries = stream.toList();
            final String connectionString = buildConnectionString(databaseName, databaseEngine);
            try (final Connection connection = DriverManager.getConnection(connectionString)) {
                for (final String query : queries) executeQuery(query, connection);

            }
        }
    }

    /**
     * Deletes test data from the database using an SQL script.
     *
     * @param databaseName   The name of the database.
     * @param databaseEngine The {@link DatabaseEngine} to be used.
     * @throws IOException  If an error occurs while reading the SQL script file.
     * @throws SQLException If an error occurs while executing SQL queries.
     */
    static void deleteData(final String databaseName, final DatabaseEngine databaseEngine)
            throws IOException, SQLException {
        final Path path = Paths.get("src/test/resources/delete_data.sql");
        try (final Stream<String> stream = Files.lines(path)) {
            final List<String> queries = stream.toList();
            final String connectionString = buildConnectionString(databaseName, databaseEngine);
            try (final Connection connection = DriverManager.getConnection(connectionString)) {
                for (final String query : queries) executeQuery(query, connection);
            }
        }
    }

    /**
     * Tests retrieval of all records from a given repository.
     *
     * @param repositoryClass The repository class to test.
     */
    static void getAllTest(final Class<?> repositoryClass) {
        // Process repository
        ConsoleUtils.printFormatted("\n------ REPOSITORY PROCESSING PHASE ------\n");
        final RepositoryMetadata repositoryMetadata = RepositoryProcessor.extractMetadata(repositoryClass);
        final DatabaseEngine databaseEngine = repositoryMetadata.databaseEngine();

        // Generate "SELECT ALL" query
        ConsoleUtils.printFormatted("\n------ GENERATING \"SELECT ALL\" QUERY PHASE ------\n");
        final SqlOperation sqlOperation = SqlOperation.SELECT_ALL;
        final SqlQueryGenerator<Optional<String>, RepositoryMetadata> selectAllQueryGenerator =
                SqlQueryGeneratorFactory.getGenerator(sqlOperation);
        final Optional<String> optionalSelectAllQuery = selectAllQueryGenerator.generate(repositoryMetadata);

        if (optionalSelectAllQuery.isEmpty()) return;

        final String selectAllQuery = optionalSelectAllQuery.get();
        final String databaseName = repositoryMetadata.databaseName();
        final var entityClass = repositoryMetadata.entityClass();

        // Execute "SELECT ALL" query
        ConsoleUtils.printFormatted("\n------ EXECUTING \"SELECT ALL\" QUERY PHASE ------\n");
        try (final var databaseConfiguration = DatabaseConfigurationFactory.getConfiguration(databaseEngine)) {
            databaseConfiguration.connect(databaseName, sqlOperation);
            final Connection connection = databaseConfiguration.getConnection();
            final var resultList = SqlQueryExecutor.executeSelectAllQuery(selectAllQuery, connection, entityClass);
            ConsoleUtils.printFormatted("\tResult: %s\n", resultList);
        }
    }

    /**
     * Tests deletion of a record by ID in a given repository.
     *
     * @param repositoryClass The repository class to test.
     * @param id              The ID of the record to delete.
     */
    static void deleteByIdTest(final Class<?> repositoryClass, final int id) {
        // Process repository
        ConsoleUtils.printFormatted("\n------ REPOSITORY PROCESSING PHASE ------\n");
        final RepositoryMetadata repositoryMetadata = RepositoryProcessor.extractMetadata(repositoryClass);
        final DatabaseEngine databaseEngine = repositoryMetadata.databaseEngine();

        // Generate "DELETE BY ID" query
        ConsoleUtils.printFormatted("\n------ GENERATING \"DELETE BY ID\" QUERY PHASE ------\n");
        final SqlOperation sqlOperation = SqlOperation.DELETE_BY_ID;
        final SqlQueryGenerator<Optional<String>, RepositoryMetadata> deleteByIdQueryGenerator =
                SqlQueryGeneratorFactory.getGenerator(sqlOperation);
        final Optional<String> optionalDeleteByIdQuery = deleteByIdQueryGenerator.generate(repositoryMetadata);

        if (optionalDeleteByIdQuery.isEmpty()) return;

        final String deleteByIdQuery = optionalDeleteByIdQuery.get();
        final String databaseName = repositoryMetadata.databaseName();

        // Execute "DELETE BY ID" query
        ConsoleUtils.printFormatted("\n------ EXECUTING \"DELETE BY ID\" QUERY PHASE ------\n");
        try (final var databaseConfiguration = DatabaseConfigurationFactory.getConfiguration(databaseEngine)) {
            databaseConfiguration.connect(databaseName, sqlOperation);
            final Connection connection = databaseConfiguration.getConnection();
            SqlQueryExecutor.executeDeleteByIdQuery(deleteByIdQuery, connection, id);
        }
    }

    /**
     * Builds a database connection string based on the database engine.
     *
     * @param databaseName   The name of the database.
     * @param databaseEngine The {@link DatabaseEngine} to be used.
     * @return The connection string for the specified database.
     */
    private static String buildConnectionString(final String databaseName, final DatabaseEngine databaseEngine) {
        String projectRoot = Paths.get("").toAbsolutePath().toString();
        return switch (databaseEngine) {
            case DERBY -> String.format("jdbc:derby:%s/db/derby/%s.db", projectRoot, databaseName);
            case SQLITE -> String.format("jdbc:sqlite:%s/db/sqlite/%s.db", projectRoot, databaseName);
            case H2 -> String.format("jdbc:h2:%s/db/h2/%s", projectRoot, databaseName);
        };
    }

    /**
     * Executes a single SQL query within a given connection.
     *
     * @param query      The SQL query to execute.
     * @param connection The database connection.
     * @throws SQLException If an error occurs while executing the query.
     */
    private static void executeQuery(final String query, final Connection connection) throws SQLException {
        try (final Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }

}
