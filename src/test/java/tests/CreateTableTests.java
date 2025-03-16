package tests;

import databases.DerbyDatabase;
import databases.H2Database;
import databases.SQLiteDatabase;
import org.junit.jupiter.api.Test;

/**
 * Test suite for simulating the ORM's table creation flow.
 * <p>
 * These tests validate that the ORM correctly processes database metadata
 * and generates the expected "CREATE TABLE" SQL statements for different database engines.
 * </p>
 * <p>
 * The primary goal is to ensure that the ORM correctly interprets entity metadata
 * and constructs appropriate SQL queries, rather than verifying actual table creation
 * in a live database.
 * </p>
 */
public class CreateTableTests extends AbstractCreateTableTest {

    /**
     * Simulates the ORM's table creation process for an H2 database.
     * <p>
     * This test verifies that the ORM processes the {@link H2Database} class correctly
     * and generates the expected table creation statements for the H2 database engine.
     * </p>
     */
    @Test
    void createH2DatabaseTest() {
        createDatabaseTest(H2Database.class);
    }

    /**
     * Simulates the ORM's table creation process for a Derby database.
     * <p>
     * This test ensures that the ORM correctly processes the {@link DerbyDatabase} class
     * and generates the necessary SQL statements for table creation in Apache Derby.
     * </p>
     */
    @Test
    void createDerbyDatabaseTest() {
        createDatabaseTest(DerbyDatabase.class);
    }

    /**
     * Simulates the ORM's table creation process for an SQLite database.
     * <p>
     * This test validates that the ORM correctly processes the {@link SQLiteDatabase} class
     * and generates the expected "CREATE TABLE" queries for SQLite.
     * </p>
     */
    @Test
    void createSqliteDatabaseTest() {
        createDatabaseTest(SQLiteDatabase.class);
    }

}
