package repositories.sqlite;

import dimstyl.orm.annotations.DeleteById;
import dimstyl.orm.annotations.Repository;
import dimstyl.orm.annotations.SelectAll;
import dimstyl.orm.enums.DatabaseEngine;
import entities.Customer;
import entities.Purchase;

import java.util.List;

/**
 * Repository interface for managing {@link Purchase} entities in the SQLite database.
 * <p>
 * This repository provides methods for retrieving and deleting purchase records.
 * It is annotated with {@link Repository} to indicate that it is an ORM repository.
 * </p>
 *
 * <p>Database Information:</p>
 * <ul>
 *     <li>Database Name: {@code sqliteDB}</li>
 *     <li>Database Engine: {@link DatabaseEngine#SQLITE}</li>
 *     <li>Entity Type: {@link Purchase}</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>{@code findAll()} - Retrieves all purchases from the database.</li>
 *     <li>{@code deleteById(int id)} - Deletes a purchase record by its ID.</li>
 * </ul>
 *
 * @see Repository
 * @see Purchase
 */
@Repository(databaseName = "sqliteDB", databaseEngine = DatabaseEngine.SQLITE, entity = Purchase.class)
public interface PurchaseRepository {

    @SelectAll
    List<Customer> findAll();

    @DeleteById
    void deleteBydId(int id);

}
