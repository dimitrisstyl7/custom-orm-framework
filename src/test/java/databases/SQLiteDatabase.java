package databases;

import dimstyl.orm.annotations.Database;
import dimstyl.orm.enums.DatabaseType;
import entities.Customer;
import entities.Purchase;

@Database(
        name = "db_3",
        type = DatabaseType.SQLITE,
        tables = {Purchase.class, Customer.class}
)
public class SQLiteDatabase {
}
