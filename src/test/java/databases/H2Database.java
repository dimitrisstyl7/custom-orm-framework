package databases;

import dimstyl.orm.annotations.Database;
import dimstyl.orm.enums.DatabaseType;
import entities.Order;
import entities.User;

@Database(
        name = "db_2",
        type = DatabaseType.H2,
        tables = {Order.class, User.class}
)
public class H2Database {
}
