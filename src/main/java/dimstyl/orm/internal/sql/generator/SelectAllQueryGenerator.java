package dimstyl.orm.internal.sql.generator;

import dimstyl.orm.enums.SqlOperation;
import dimstyl.orm.internal.utils.ConsoleUtils;
import dimstyl.orm.metadata.RepositoryMetadata;

import java.util.Optional;
import java.util.Set;

/**
 * Generates an SQL `SELECT * FROM` query for retrieving all records from a table.
 * <p>
 * This generator ensures that the repository supports the `SELECT_ALL` operation before
 * creating the query.
 * If the operation is not supported, query generation is skipped.
 * </p>
 */
enum SelectAllQueryGenerator implements SqlQueryGenerator<Optional<String>, RepositoryMetadata> {

    /**
     * The singleton instance of the SelectAllQueryGenerator.
     */
    INSTANCE;

    /**
     * Generates an SQL `SELECT * FROM` query if the repository supports the `SELECT_ALL` operation.
     * <p>
     * If the repository does not support this operation, query generation is skipped,
     * and an empty {@link Optional} is returned.
     * </p>
     *
     * @param repositoryMetadata Metadata containing information about the repository, including the table name and supported operations,
     *                           as defined by {@link RepositoryMetadata}.
     * @return An {@link Optional} containing the SQL query if the `SELECT_ALL`
     * operation is supported, otherwise an empty {@link Optional}.
     */
    @Override
    public Optional<String> generate(final RepositoryMetadata repositoryMetadata) {
        ConsoleUtils.printFormatted("\n🔄️ Generating SQL 'SELECT ALL' query...\n");
        final Set<SqlOperation> supportedOperations = repositoryMetadata.supportedOperations();
        final String tableName = repositoryMetadata.tableName();
        final boolean SelectAllOperationExists = supportedOperations.contains(SqlOperation.SELECT_ALL);

        if (!SelectAllOperationExists) {
            ConsoleUtils.printFormatted("\t➡️ SQL 'SELECT ALL' operation is not supported for this repository. Skipping query generation.\n");
            return Optional.empty();
        }

        ConsoleUtils.printFormatted("✅ SQL query generated successfully\n");
        return Optional.of("SELECT * FROM " + tableName);
    }

}
