import dimstyl.orm.metadata.DatabaseMetadata;
import dimstyl.orm.processors.DatabaseAnnotationProcessor;
import org.junit.jupiter.api.Test;

public class H2Tests {

    @Test
    void Test() {
        final DatabaseMetadata databaseMetadata = DatabaseAnnotationProcessor.process(AppDatabase.class);
    }

}
