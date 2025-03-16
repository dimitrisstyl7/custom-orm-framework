package dimstyl.orm.internal.sql.generator;

import dimstyl.orm.annotations.Column;
import dimstyl.orm.enums.SqlOperation;
import dimstyl.orm.exceptions.CompositePrimaryKeyException;
import dimstyl.orm.exceptions.MissingColumnAnnotationException;
import dimstyl.orm.exceptions.MissingPrimaryKeyException;
import dimstyl.orm.internal.processors.TableProcessor;
import dimstyl.orm.internal.utils.ConsoleUtils;
import dimstyl.orm.metadata.RepositoryMetadata;

import java.util.Optional;
import java.util.Set;

/**
 * Generates an SQL `DELETE FROM` query for deleting an entity by its primary key.
 * <p>
 * This generator ensures that the repository supports the `DELETE_BY_ID` operation before
 * creating the query.
 * If the operation is not supported, query generation is skipped.
 * </p>
 */
enum DeleteByIdQueryGenerator implements SqlQueryGenerator<Optional<String>, RepositoryMetadata> {

    /**
     * The singleton instance of the DeleteByIdQueryGenerator.
     */
    INSTANCE;

    /**
     * Generates an SQL `DELETE BY ID` query for a given entity repository.
     * <p>
     * The method checks if the repository supports the `DELETE_BY_ID` operation before proceeding.
     * If the entity has a composite primary key, an exception is thrown since this method only
     * supports single-column primary keys.
     * </p>
     *
     * @param repositoryMetadata Metadata about the repository, including table and entity information, as defined by {@link RepositoryMetadata}.
     * @return An {@link Optional} containing the SQL query if the `DELETE_BY_ID`
     * operation is supported, otherwise an empty {@link Optional}.
     * @throws CompositePrimaryKeyException     If the entity has a composite primary key.
     * @throws MissingColumnAnnotationException If the primary key field is missing the required {@link Column} annotation.
     * @throws MissingPrimaryKeyException       If the entity does not have a defined primary key.
     */
    @Override
    public Optional<String> generate(final RepositoryMetadata repositoryMetadata)
            throws CompositePrimaryKeyException, MissingColumnAnnotationException, MissingPrimaryKeyException {
        ConsoleUtils.printFormatted("\n🔄️ Generating SQL 'DELETE BY ID' query...\n");
        final Set<SqlOperation> supportedOperations = repositoryMetadata.supportedOperations();
        final String tableName = repositoryMetadata.tableName();
        final boolean deleteBydIdOperationExists = supportedOperations.contains(SqlOperation.DELETE_BY_ID);

        if (!deleteBydIdOperationExists) {
            ConsoleUtils.printFormatted("\t➡️ SQL 'DELETE BY ID' operation is not supported for this repository. Skipping query generation.\n");
            return Optional.empty();
        }

        final var entityClass = repositoryMetadata.entityClass();
        final String columnName = TableProcessor.resolvePrimaryKeyColumnName(entityClass);

        ConsoleUtils.printFormatted("✅ SQL query generated successfully\n");
        return Optional.of(String.format("DELETE FROM %s WHERE %s = ?", tableName, columnName));
    }

}
