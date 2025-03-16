package databases;

import dimstyl.orm.annotations.Database;
import dimstyl.orm.enums.DatabaseEngine;
import entities.Customer;
import entities.Purchase;

@Database(
        name = "derbyDB",
        engine = DatabaseEngine.DERBY,
        tables = {Purchase.class, Customer.class}
)
public class DerbyDatabase {
}
