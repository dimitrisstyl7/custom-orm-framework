package dimstyl.orm.metadata;

import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.enums.SqlOperation;
import dimstyl.orm.model.Entity;
import lombok.Builder;

import java.util.Set;

/**
 * Represents metadata for a repository, including database details, table name,
 * associated entity class, and supported SQL operations.
 * <p>
 * This record provides metadata for a repository, containing information about the
 * database name, database engine, table name, entity class, and the set of
 * supported SQL operations.
 * </p>
 *
 * @param databaseName        The name of the database.
 * @param databaseEngine      The {@link DatabaseEngine} used (e.g., H2, SQLite, Derby).
 * @param tableName           The name of the table associated with this repository.
 * @param entityClass         The entity class linked to this repository.
 * @param supportedOperations The set of {@link SqlOperation} supported by this repository.
 */
@Builder
public record RepositoryMetadata(String databaseName,
                                 DatabaseEngine databaseEngine,
                                 String tableName,
                                 Class<? extends Entity> entityClass,
                                 Set<SqlOperation> supportedOperations) implements Metadata {
}
