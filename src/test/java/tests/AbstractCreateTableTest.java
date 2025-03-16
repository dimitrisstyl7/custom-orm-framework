package tests;

import dimstyl.orm.configuration.DatabaseConfigurationFactory;
import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.internal.processors.DatabaseProcessor;
import dimstyl.orm.internal.sql.execution.SqlQueryExecutor;
import dimstyl.orm.internal.sql.generator.SqlQueryGenerator;
import dimstyl.orm.internal.sql.generator.SqlQueryGeneratorFactory;
import dimstyl.orm.internal.utils.ConsoleUtils;
import dimstyl.orm.metadata.DatabaseMetadata;

import java.sql.Connection;
import java.util.List;

/**
 * Abstract test class for testing the creation of database tables using ORM metadata.
 * <p>
 * This class is responsible for processing a given database class, generating SQL "CREATE TABLE" queries,
 * and executing them within the appropriate database engine.
 * </p>
 *
 * <p>Test Workflow:</p>
 * <ol>
 *     <li>Extract metadata from the provided database class.</li>
 *     <li>Generate SQL "CREATE TABLE" queries.</li>
 *     <li>Execute the generated queries within a database connection.</li>
 * </ol>
 *
 * @see DatabaseProcessor
 * @see SqlQueryGenerator
 * @see SqlQueryExecutor
 * @see DatabaseConfigurationFactory
 */
abstract class AbstractCreateTableTest extends AbstractTest {

    /**
     * Executes a test to create tables in the database based on the provided database class.
     *
     * @param databaseClass The class annotated as a database that contains table definitions.
     */
    static void createDatabaseTest(final Class<?> databaseClass) {
        // Process database
        ConsoleUtils.printFormatted("\n------ DATABASE PROCESSING PHASE ------\n");
        final DatabaseMetadata databaseMetadata = DatabaseProcessor.extractMetadata(databaseClass);

        // Generate "CREATE TABLE" queries
        ConsoleUtils.printFormatted("\n------ GENERATING \"CREATE TABLE\" QUERIES PHASE ------\n");
        final SqlQueryGenerator<List<String>, DatabaseMetadata> sqlQueryGenerator =
                SqlQueryGeneratorFactory.getGenerator(databaseMetadata.sqlOperation());
        final List<String> createTableQueries = sqlQueryGenerator.generate(databaseMetadata);
        final String databaseName = databaseMetadata.databaseName();
        final DatabaseEngine databaseEngine = databaseMetadata.databaseEngine();

        // Execute "CREATE TABLE" queries
        ConsoleUtils.printFormatted("\n------ EXECUTING \"CREATE TABLE\" QUERIES PHASE ------\n");
        try (final var databaseConfiguration = DatabaseConfigurationFactory.getConfiguration(databaseEngine)) {
            databaseConfiguration.connect(databaseName, databaseMetadata.sqlOperation());
            final Connection connection = databaseConfiguration.getConnection();
            SqlQueryExecutor.executeCreateTableQueries(createTableQueries, connection);
        }
    }

}
