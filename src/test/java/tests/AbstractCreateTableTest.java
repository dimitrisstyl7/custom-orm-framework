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

abstract class AbstractCreateTableTest extends AbstractTest {

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
