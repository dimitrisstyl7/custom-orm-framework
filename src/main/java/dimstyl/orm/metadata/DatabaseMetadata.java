package dimstyl.orm.metadata;

import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.enums.SqlOperation;

import java.util.List;

/**
 * Represents metadata for a database, including its name, engine, SQL operations, and tables.
 * <p>
 * This record is used to store metadata about a database, including its name, database engine,
 * supported SQL operations, and the list of tables it contains.
 * </p>
 *
 * @param databaseName      The name of the database.
 * @param databaseEngine    The database engine used (e.g., H2, SQLite, Derby).
 * @param sqlOperation      The SQL operation associated with this metadata.
 * @param tableMetadataList The list of table metadata objects.
 */
public record DatabaseMetadata(String databaseName,
                               DatabaseEngine databaseEngine,
                               SqlOperation sqlOperation,
                               List<TableMetadata> tableMetadataList) implements Metadata {

    /**
     * Adds a new table metadata to the database.
     *
     * @param tableMetadata The {@link TableMetadata} to add.
     * @throws NullPointerException if tableMetadata is null.
     */
    public void addTableMetadata(TableMetadata tableMetadata) {
        tableMetadataList.add(tableMetadata);
    }

}