package databases;

import dimstyl.orm.annotations.Database;
import dimstyl.orm.enums.DatabaseType;
import entities.Customer;
import entities.Purchase;

@Database(
        name = "db_2",
        type = DatabaseType.H2,
        tables = {Purchase.class, Customer.class}
)
public class H2Database {
}
