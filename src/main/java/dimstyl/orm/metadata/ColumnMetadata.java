package dimstyl.orm.metadata;

import lombok.Builder;

@Builder
public record ColumnMetadata(String columnName,
                             String columnType,
                             boolean primaryKey,
                             boolean nullable,
                             boolean unique) {
}