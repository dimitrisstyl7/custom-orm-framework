package tests;

import dimstyl.orm.enums.DatabaseEngine;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Test suite for simulating entity retrieval and deletion using the ORM.
 * <p>
 * These tests validate that the ORM correctly processes repositories for different
 * database engines (H2, Derby, SQLite) and generates appropriate SQL queries for
 * retrieving and deleting entities.
 * </p>
 * <p>
 * The focus of these tests is to ensure that the ORM correctly handles entity retrieval
 * and deletion workflows, rather than executing full CRUD (Create, Read, Update, Delete) operations.
 * </p>
 */
public class CrudTests extends AbstractCrudTest {

    /**
     * Seeds initial data into all databases before running tests.
     *
     * @throws SQLException If there is an error executing SQL queries.
     * @throws IOException  If there is an error reading seed data from a file.
     */
    @BeforeAll
    static void beforeAll() throws SQLException, IOException {
        seedData("h2DB", DatabaseEngine.H2);
        seedData("derbyDB", DatabaseEngine.DERBY);
        seedData("sqliteDB", DatabaseEngine.SQLITE);
    }

    /**
     * Cleans up database records after all tests have been executed.
     *
     * @throws SQLException If there is an error executing SQL queries.
     * @throws IOException  If there is error reading delete data from the file.
     */
    @AfterAll
    static void afterAll() throws SQLException, IOException {
        deleteData("h2DB", DatabaseEngine.H2);
        deleteData("derbyDB", DatabaseEngine.DERBY);
        deleteData("sqliteDB", DatabaseEngine.SQLITE);
    }

    /* ------------- H2 tests ------------- */

    /**
     * Simulates retrieving all customers from the H2 database.
     */
    @Test
    void getAllCustomersH2Test() {
        getAllTest(repositories.h2.CustomerRepository.class);
    }

    /**
     * Simulates deleting a customer by ID from the H2 database.
     */
    @Test
    void deleteCustomerByIdH2Test() {
        deleteByIdTest(repositories.h2.CustomerRepository.class, 1);
    }

    /**
     * Simulates retrieving all purchases from the H2 database.
     */
    @Test
    void getAllPurchasesH2Test() {
        getAllTest(repositories.h2.PurchaseRepository.class);
    }

    /**
     * Simulates deleting a purchase by ID from the H2 database.
     */
    @Test
    void deletePurchaseByIdH2Test() {
        deleteByIdTest(repositories.h2.PurchaseRepository.class, 1);
    }

    /* ------------- Derby tests ------------- */

    /**
     * Simulates retrieving all customers from the Derby database.
     */
    @Test
    void getAllCustomersDerbyTest() {
        getAllTest(repositories.derby.CustomerRepository.class);
    }

    /**
     * Simulates deleting a customer by ID from the Derby database.
     */
    @Test
    void deleteCustomerByIdDerbyTest() {
        deleteByIdTest(repositories.derby.CustomerRepository.class, 1);
    }

    /**
     * Simulates retrieving all purchases from the Derby database.
     */
    @Test
    void getAllPurchasesDerbyTest() {
        getAllTest(repositories.derby.PurchaseRepository.class);
    }

    /**
     * Simulates deleting a purchase by ID from the Derby database.
     */
    @Test
    void deletePurchaseByIdDerbyTest() {
        deleteByIdTest(repositories.derby.PurchaseRepository.class, 1);
    }

    /* ------------- SQLite tests ------------- */

    /**
     * Simulates retrieving all customers from the SQLite database.
     */
    @Test
    void getAllCustomersSqliteTest() {
        getAllTest(repositories.sqlite.CustomerRepository.class);
    }

    /**
     * Simulates deleting a customer by ID from the SQLite database.
     */
    @Test
    void deleteCustomerByIdSqliteTest() {
        deleteByIdTest(repositories.sqlite.CustomerRepository.class, 1);
    }

    /**
     * Simulates retrieving all purchases from the SQLite database.
     */
    @Test
    void getAllPurchasesSqliteTest() {
        getAllTest(repositories.sqlite.PurchaseRepository.class);
    }

    /**
     * Simulates deleting a purchase by ID from the SQLite database.
     */
    @Test
    void deletePurchaseByIdSqliteTest() {
        deleteByIdTest(repositories.sqlite.PurchaseRepository.class, 1);
    }

}
