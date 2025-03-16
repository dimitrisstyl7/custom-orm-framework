package dimstyl.orm.internal.sql.generator;

import dimstyl.orm.enums.SqlOperation;
import dimstyl.orm.metadata.Metadata;

public final class SqlQueryGeneratorFactory {

    private SqlQueryGeneratorFactory() {
    }

    public static <K, T extends Metadata> SqlQueryGenerator<K, T> getGenerator(final SqlOperation sqlOperation) {
        return (SqlQueryGenerator<K, T>) switch (sqlOperation) {
            case CREATE_TABLE -> DatabaseSchemaGenerator.INSTANCE;
            case SELECT_ALL -> SelectAllQueryGenerator.INSTANCE;
            case DELETE_BY_ID -> DeleteByIdQueryGenerator.INSTANCE;
        };
    }

}
