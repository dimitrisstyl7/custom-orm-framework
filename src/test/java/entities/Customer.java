package entities;

import dimstyl.orm.annotations.Column;
import dimstyl.orm.annotations.PrimaryKey;
import dimstyl.orm.annotations.Table;
import dimstyl.orm.model.Entity;
import lombok.ToString;

/**
 * Represents a customer entity in the database.
 * <p>
 * This class is annotated with {@link Table} to define the corresponding table name
 * and {@link Column} annotations to specify the properties of each column.
 * It implements {@link Entity}, indicating that it is a persistable ORM entity.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>{@code id} - The primary key for the customer table.</li>
 *     <li>{@code firstName} - The first name of the customer (not nullable).</li>
 *     <li>{@code lastName} - The last name of the customer.</li>
 *     <li>{@code email} - The email of the customer (unique, not nullable).</li>
 *     <li>{@code active} - A flag indicating whether the customer is active.</li>
 * </ul>
 *
 * @see Entity
 * @see Table
 */
@ToString
@Table(name = "customer")
public class Customer implements Entity {

    @PrimaryKey
    @Column(nullable = false, unique = true)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private boolean active;

}
