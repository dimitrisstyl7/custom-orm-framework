package tests;

import dimstyl.orm.enums.DatabaseEngine;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

public class CrudTests extends AbstractCrudTest {

    @BeforeAll
    static void beforeAll() throws SQLException, IOException {
        seedData("h2DB", DatabaseEngine.H2);
        seedData("derbyDB", DatabaseEngine.DERBY);
        seedData("sqliteDB", DatabaseEngine.SQLITE);
    }

    @AfterAll
    static void afterAll() throws SQLException, IOException {
        deleteData("h2DB", DatabaseEngine.H2);
        deleteData("derbyDB", DatabaseEngine.DERBY);
        deleteData("sqliteDB", DatabaseEngine.SQLITE);
    }

    /* ------------- H2 tests ------------- */

    @Test
    void getAllCustomersH2Test() {
        getAllTest(repositories.h2.CustomerRepository.class);
    }

    @Test
    void deleteCustomerByIdH2Test() {
        deleteByIdTest(repositories.h2.CustomerRepository.class, 1);
    }

    @Test
    void getAllPurchasesH2Test() {
        getAllTest(repositories.h2.PurchaseRepository.class);
    }

    @Test
    void deletePurchaseByIdH2Test() {
        deleteByIdTest(repositories.h2.PurchaseRepository.class, 1);
    }

    /* ------------- Derby tests ------------- */

    @Test
    void getAllCustomersDerbyTest() {
        getAllTest(repositories.derby.CustomerRepository.class);
    }

    @Test
    void deleteCustomerByIdDerbyTest() {
        deleteByIdTest(repositories.derby.CustomerRepository.class, 1);
    }

    @Test
    void getAllPurchasesDerbyTest() {
        getAllTest(repositories.derby.PurchaseRepository.class);
    }

    @Test
    void deletePurchaseByIdDerbyTest() {
        deleteByIdTest(repositories.derby.PurchaseRepository.class, 1);
    }

    /* ------------- SQLite tests ------------- */

    @Test
    void getAllCustomersSqliteTest() {
        getAllTest(repositories.sqlite.CustomerRepository.class);
    }

    @Test
    void deleteCustomerByIdSqliteTest() {
        deleteByIdTest(repositories.sqlite.CustomerRepository.class, 1);
    }

    @Test
    void getAllPurchasesSqliteTest() {
        getAllTest(repositories.sqlite.PurchaseRepository.class);
    }

    @Test
    void deletePurchaseByIdSqliteTest() {
        deleteByIdTest(repositories.sqlite.PurchaseRepository.class, 1);
    }

}
