package repositories.derby;

import dimstyl.orm.annotations.Repository;
import dimstyl.orm.annotations.SelectAll;
import dimstyl.orm.enums.DatabaseEngine;
import entities.Customer;

import java.util.List;

/**
 * Repository interface for managing {@link Customer} entities in the Derby database.
 * <p>
 * This repository provides methods for retrieving and managing customer data.
 * It is annotated with {@link Repository} to indicate that it is an ORM repository.
 * </p>
 *
 * <p>Database Information:</p>
 * <ul>
 *     <li>Database Name: {@code derbyDB}</li>
 *     <li>Database Engine: {@link DatabaseEngine#DERBY}</li>
 *     <li>Entity Type: {@link Customer}</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>{@code findAll()} - Retrieves all customers from the database.</li>
 * </ul>
 *
 * @see Repository
 * @see Customer
 */
@Repository(databaseName = "derbyDB", databaseEngine = DatabaseEngine.DERBY, entity = Customer.class)
public interface CustomerRepository {

    @SelectAll
    List<Customer> findAll();

}
