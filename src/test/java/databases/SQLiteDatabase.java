package databases;

import dimstyl.orm.annotations.Database;
import dimstyl.orm.enums.DatabaseEngine;
import entities.Customer;
import entities.Purchase;

@Database(
        name = "sqliteDB",
        engine = DatabaseEngine.SQLITE,
        tables = {Purchase.class, Customer.class}
)
public class SQLiteDatabase {
}
