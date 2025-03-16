package databases;

import dimstyl.orm.annotations.Database;
import dimstyl.orm.enums.DatabaseEngine;
import entities.Customer;
import entities.Purchase;

@Database(
        name = "h2DB",
        engine = DatabaseEngine.H2,
        tables = {Purchase.class, Customer.class}
)
public class H2Database {
}
