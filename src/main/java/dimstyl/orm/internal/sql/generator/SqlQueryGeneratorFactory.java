package dimstyl.orm.internal.sql.generator;

import dimstyl.orm.enums.SqlOperation;
import dimstyl.orm.metadata.Metadata;

/**
 * Factory class for retrieving instances of SQL query generators based on the specified SQL operation.
 *
 * <p>This factory provides a centralized way to obtain the appropriate {@link SqlQueryGenerator}
 * instance based on the given {@link SqlOperation}. It supports operations such as:
 * <ul>
 *     <li>{@code CREATE_TABLE} - Uses {@link DatabaseSchemaGenerator#INSTANCE}</li>
 *     <li>{@code SELECT_ALL} - Uses {@link SelectAllQueryGenerator#INSTANCE}</li>
 *     <li>{@code DELETE_BY_ID} - Uses {@link DeleteByIdQueryGenerator#INSTANCE}</li>
 * </ul>
 * </p>
 *
 * <p><strong>Usage Example:</strong></p>
 * <pre>
 *     SqlQueryGenerator&lt;List<String>, DatabaseMetadata&gt; generator =
 *                  SqlQueryGeneratorFactory.getGenerator(SqlOperation.CREATE_TABLE);
 * </pre>
 */
public final class SqlQueryGeneratorFactory {

    /**
     * Private constructor to prevent instantiation of the factory class.
     */
    private SqlQueryGeneratorFactory() {
    }

    /**
     * Retrieves the appropriate SQL query generator based on the provided SQL operation.
     *
     * @param sqlOperation The SQL operation for which a generator is needed.
     * @param <K>          The type of the result produced by the query generator.
     * @param <T>          The type of metadata used in query generation, extending {@link Metadata}.
     * @return An instance of {@link SqlQueryGenerator} corresponding to the given {@link SqlOperation}.
     */
    public static <K, T extends Metadata> SqlQueryGenerator<K, T> getGenerator(final SqlOperation sqlOperation) {
        return (SqlQueryGenerator<K, T>) switch (sqlOperation) {
            case CREATE_TABLE -> DatabaseSchemaGenerator.INSTANCE;
            case SELECT_ALL -> SelectAllQueryGenerator.INSTANCE;
            case DELETE_BY_ID -> DeleteByIdQueryGenerator.INSTANCE;
        };
    }

}
