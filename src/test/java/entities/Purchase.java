package entities;

import dimstyl.orm.annotations.Column;
import dimstyl.orm.annotations.PrimaryKey;
import dimstyl.orm.annotations.Table;
import dimstyl.orm.annotations.UniqueConstraint;
import dimstyl.orm.model.Entity;
import lombok.ToString;

/**
 * Represents a purchase entity in the database.
 * <p>
 * This class is annotated with {@link Table} to define the corresponding table
 * and includes a {@link UniqueConstraint} on the combination of {@code customerId} and {@code createdAt}.
 * It implements {@link Entity}, indicating that it is a persistable ORM entity.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>{@code id} - The primary key for the purchase table.</li>
 *     <li>{@code customerId} - The ID of the customer who made the purchase (not nullable).</li>
 *     <li>{@code createdAt} - The timestamp when the purchase was made (not nullable).</li>
 * </ul>
 *
 * @see Entity
 * @see Table
 * @see UniqueConstraint
 */
@ToString
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"customer_id", "created_at"}))
public class Purchase implements Entity {

    @PrimaryKey
    @Column(nullable = false)
    private int id;

    @Column(nullable = false)
    private Integer customerId;

    @Column(nullable = false)
    private String createdAt;

}
