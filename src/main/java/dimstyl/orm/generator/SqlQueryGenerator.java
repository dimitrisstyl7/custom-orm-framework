package dimstyl.orm.generator;

import dimstyl.orm.exceptions.InvalidColumnNameException;
import dimstyl.orm.metadata.DatabaseMetadata;

import java.util.List;

public interface SqlQueryGenerator {

    List<String> generateDatabaseSchema(final DatabaseMetadata databaseMetadata)
            throws InvalidColumnNameException;

}
