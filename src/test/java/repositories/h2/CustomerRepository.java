package repositories.h2;

import dimstyl.orm.annotations.DeleteById;
import dimstyl.orm.annotations.Repository;
import dimstyl.orm.annotations.SelectAll;
import dimstyl.orm.enums.DatabaseEngine;
import entities.Customer;

import java.util.List;

/**
 * Repository interface for managing {@link Customer} entities in the H2 database.
 * <p>
 * This repository provides methods for retrieving and deleting customer records.
 * It is annotated with {@link Repository} to indicate that it is an ORM repository.
 * </p>
 *
 * <p>Database Information:</p>
 * <ul>
 *     <li>Database Name: {@code h2DB}</li>
 *     <li>Database Engine: {@link DatabaseEngine#H2}</li>
 *     <li>Entity Type: {@link Customer}</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>{@code findAll()} - Retrieves all customers from the database.</li>
 *     <li>{@code deleteById(int id)} - Deletes a customer record by its ID.</li>
 * </ul>
 *
 * @see Repository
 * @see Customer
 */
@Repository(databaseName = "h2DB", databaseEngine = DatabaseEngine.H2, entity = Customer.class)
public interface CustomerRepository {

    @SelectAll
    List<Customer> findAll();

    @DeleteById
    void deleteBydId(int id);

}
