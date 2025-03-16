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

abstract class AbstractCrudTest extends AbstractTest {

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

    private static String buildConnectionString(final String databaseName, final DatabaseEngine databaseEngine) {
        String projectRoot = Paths.get("").toAbsolutePath().toString();
        return switch (databaseEngine) {
            case DERBY -> String.format("jdbc:derby:%s/db/derby/%s.db", projectRoot, databaseName);
            case SQLITE -> String.format("jdbc:sqlite:%s/db/sqlite/%s.db", projectRoot, databaseName);
            case H2 -> String.format("jdbc:h2:%s/db/h2/%s", projectRoot, databaseName);
        };
    }

    private static void executeQuery(final String query, final Connection connection) throws SQLException {
        try (final Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }

}
