package databases;

import dimstyl.orm.annotations.Database;
import dimstyl.orm.enums.DatabaseEngine;
import entities.Customer;
import entities.Purchase;

/**
 * Represents an SQLite database configuration for the ORM.
 * <p>
 * This class is annotated with {@link Database} to define the database properties,
 * including its name, engine type, and associated entity tables.
 * </p>
 * <p>
 * {@link Database} Specifies that this class represents a database configuration.
 */
@Database(
        name = "sqliteDB",
        engine = DatabaseEngine.SQLITE,
        tables = {Purchase.class, Customer.class}
)
public class SQLiteDatabase {
}
