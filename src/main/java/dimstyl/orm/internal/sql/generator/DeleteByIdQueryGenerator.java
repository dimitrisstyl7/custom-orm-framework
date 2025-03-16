package dimstyl.orm.internal.sql.generator;

import dimstyl.orm.enums.SqlOperation;
import dimstyl.orm.exceptions.CompositePrimaryKeyException;
import dimstyl.orm.exceptions.MissingColumnAnnotationException;
import dimstyl.orm.exceptions.MissingPrimaryKeyException;
import dimstyl.orm.internal.processors.TableProcessor;
import dimstyl.orm.internal.utils.ConsoleUtils;
import dimstyl.orm.metadata.RepositoryMetadata;

import java.util.Optional;
import java.util.Set;

enum DeleteByIdQueryGenerator implements SqlQueryGenerator<Optional<String>, RepositoryMetadata> {

    INSTANCE;

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
