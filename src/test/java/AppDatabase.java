import dimstyl.orm.annotations.Database;
import dimstyl.orm.enums.DatabaseType;
import entities.Order;
import entities.User;

@Database(
        name = "app_db",
        type = DatabaseType.H2,
        tables = {Order.class, User.class}
)
public class AppDatabase {
}
