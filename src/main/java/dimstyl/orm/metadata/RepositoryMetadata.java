package dimstyl.orm.metadata;

import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.enums.SqlOperation;
import dimstyl.orm.model.Entity;
import lombok.Builder;

import java.util.Set;

@Builder
public record RepositoryMetadata(String databaseName,
                                 DatabaseEngine databaseEngine,
                                 String tableName,
                                 Class<? extends Entity> entityClass,
                                 Set<SqlOperation> supportedOperations) implements Metadata {
}
