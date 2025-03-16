package dimstyl.orm.metadata;

import lombok.Builder;

/**
 * Represents metadata for a database column.
 *
 * <p>This record encapsulates essential details about a column,
 * including its name, data type, constraints (e.g., primary key, nullable, unique).</p>
 *
 * @param columnName The name of the column.
 * @param columnType The data type of the column (e.g., "INTEGER", "VARCHAR").
 * @param primaryKey {@code true} if the column is a primary key, {@code false} otherwise.
 * @param nullable   {@code true} if the column can have {@code NULL} values, {@code false} otherwise.
 * @param unique     {@code true} if the column has a unique constraint, {@code false} otherwise.
 */
@Builder
public record ColumnMetadata(String columnName,
                             String columnType,
                             boolean primaryKey,
                             boolean nullable,
                             boolean unique) implements Metadata {
}