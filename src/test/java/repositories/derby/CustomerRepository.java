package repositories.derby;

import dimstyl.orm.annotations.DeleteById;
import dimstyl.orm.annotations.Repository;
import dimstyl.orm.annotations.SelectAll;
import dimstyl.orm.enums.DatabaseEngine;
import entities.Customer;

import java.util.List;

@Repository(databaseName = "derbyDB", databaseEngine = DatabaseEngine.DERBY, entity = Customer.class)
public interface CustomerRepository {

    @SelectAll
    List<Customer> findAll();

}
