import databases.DerbyDatabase;
import databases.H2Database;
import databases.SQLiteDatabase;
import dimstyl.orm.metadata.DatabaseMetadata;
import dimstyl.orm.processors.DatabaseAnnotationProcessor;
import org.junit.jupiter.api.Test;

public class DatabasesTests {

    @Test
    void H2Test() {
        final DatabaseMetadata databaseMetadata = DatabaseAnnotationProcessor.process(H2Database.class);
    }

    @Test
    void SQLiteTest() {
        final DatabaseMetadata databaseMetadata = DatabaseAnnotationProcessor.process(SQLiteDatabase.class);
    }

    @Test
    void DerbyTest() {
        final DatabaseMetadata databaseMetadata = DatabaseAnnotationProcessor.process(DerbyDatabase.class);
    }

}
