package tests;

import databases.DerbyDatabase;
import databases.H2Database;
import databases.SQLiteDatabase;
import org.junit.jupiter.api.Test;

public class CreateTableTests extends AbstractCreateTableTest {

    @Test
    void createH2DatabaseTest() {
        createDatabaseTest(H2Database.class);
    }

    @Test
    void createDerbyDatabaseTest() {
        createDatabaseTest(DerbyDatabase.class);
    }

    @Test
    void createSqliteDatabaseTest() {
        createDatabaseTest(SQLiteDatabase.class);
    }

}
