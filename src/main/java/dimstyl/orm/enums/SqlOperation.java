package dimstyl.orm.enums;

/**
 * Enum representing different SQL operations supported by the ORM framework.
 * <p>
 * This enum defines the various types of SQL operations that can be executed,
 * such as creating tables, selecting all records, and deleting by ID.
 * </p>
 *
 * <p><strong>Possible Values:</strong></p>
 * <ul>
 *     <li>{@link #CREATE_TABLE} - Represents an operation to create a new table in the database.</li>
 *     <li>{@link #SELECT_ALL} - Represents an operation to select all records from a database table.</li>
 *     <li>{@link #DELETE_BY_ID} - Represents an operation to delete a record from a table based on its primary key.</li>
 * </ul>
 */
public enum SqlOperation {

    /**
     * Represents an operation to create a new table in the database.
     */
    CREATE_TABLE,

    /**
     * Represents an operation to select all records from a database table.
     */
    SELECT_ALL,

    /**
     * Represents an operation to delete a record from a table based on its primary key.
     */
    DELETE_BY_ID

}
