import databases.DerbyDatabase;
import databases.H2Database;
import databases.SQLiteDatabase;
import dimstyl.orm.configuration.DatabaseConfigurationFactory;
import dimstyl.orm.enums.DatabaseType;
import dimstyl.orm.executor.SqlQueryExecutor;
import dimstyl.orm.generator.SqlQueryGenerator;
import dimstyl.orm.generator.SqlQueryGeneratorFactory;
import dimstyl.orm.metadata.DatabaseMetadata;
import dimstyl.orm.processors.DatabaseAnnotationProcessor;
import dimstyl.orm.utils.ConsoleUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

class DatabasesTests {

    private static void runTest(Class<?> databaseClass) {
        // Process database
        ConsoleUtils.printFormatted("\n------ DATABASE PROCESSING PHASE ------\n");

        final DatabaseMetadata databaseMetadata = DatabaseAnnotationProcessor.process(databaseClass);

        // Generate "CREATE TABLE" queries
        ConsoleUtils.printFormatted("\n------ GENERATING \"CREATE TABLE\" QUERIES PHASE ------\n");

        final SqlQueryGenerator sqlQueryGenerator = SqlQueryGeneratorFactory.getGenerator();
        final List<String> createTableQueries = sqlQueryGenerator.generateDatabaseSchema(databaseMetadata);

        // Execute "CREATE TABLE" queries
        ConsoleUtils.printFormatted("\n------ EXECUTING \"CREATE TABLE\" QUERIES PHASE ------\n");

        final String databaseName = databaseMetadata.databaseName();
        final DatabaseType databaseType = databaseMetadata.databaseType();

        try (final var databaseConfiguration = DatabaseConfigurationFactory.getConfiguration(databaseType)) {
            databaseConfiguration.connect(databaseName);
            SqlQueryExecutor.executeCreateTableQueries(createTableQueries, databaseConfiguration.getConnection());
        }
    }

    @Test
    void H2Test() {
        runTest(H2Database.class);
    }

    @Test
    void SQLiteTest() {
        runTest(SQLiteDatabase.class);
    }

    @Test
    void DerbyTest() {
        runTest(DerbyDatabase.class);
    }

}
