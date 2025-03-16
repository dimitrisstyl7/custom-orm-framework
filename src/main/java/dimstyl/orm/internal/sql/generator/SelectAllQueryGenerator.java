package dimstyl.orm.internal.sql.generator;

import dimstyl.orm.enums.SqlOperation;
import dimstyl.orm.internal.utils.ConsoleUtils;
import dimstyl.orm.metadata.RepositoryMetadata;

import java.util.Optional;
import java.util.Set;

enum SelectAllQueryGenerator implements SqlQueryGenerator<Optional<String>, RepositoryMetadata> {

    INSTANCE;

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
