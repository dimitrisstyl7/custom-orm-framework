package databases;

import dimstyl.orm.annotations.Database;
import dimstyl.orm.enums.DatabaseType;
import entities.Order;
import entities.User;

@Database(
        name = "db_1",
        type = DatabaseType.DERBY,
        tables = {Order.class, User.class}
)
public class DerbyDatabase {
}
