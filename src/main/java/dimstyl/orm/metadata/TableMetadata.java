package dimstyl.orm.metadata;

import dimstyl.orm.annotations.UniqueConstraint;

import java.util.List;

/**
 * Represents metadata for a database table, including its name, unique constraints,
 * and associated column metadata.
 * <p>
 * This record provides structural details about a database table, such as the table name,
 * unique constraints, and the list of column metadata associated with it.
 * </p>
 *
 * @param tableName          The name of the table.
 * @param uniqueConstraints  An array of {@link UniqueConstraint} applied to the table.
 * @param columnMetadataList A list containing {@link ColumnMetadata} for all columns in the table.
 */
public record TableMetadata(String tableName,
                            UniqueConstraint[] uniqueConstraints,
                            List<ColumnMetadata> columnMetadataList) implements Metadata {

    /**
     * Adds a column's metadata to the table's column metadata list.
     *
     * @param columnMetadata The {@link ColumnMetadata} to be added.
     * @throws NullPointerException if {@code columnMetadata} is null.
     */
    public void addColumnMetadata(ColumnMetadata columnMetadata) {
        columnMetadataList.add(columnMetadata);
    }

}