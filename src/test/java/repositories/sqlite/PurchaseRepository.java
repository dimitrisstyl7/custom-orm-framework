package repositories.sqlite;

import dimstyl.orm.annotations.DeleteById;
import dimstyl.orm.annotations.Repository;
import dimstyl.orm.annotations.SelectAll;
import dimstyl.orm.enums.DatabaseEngine;
import entities.Customer;
import entities.Purchase;

import java.util.List;

@Repository(databaseName = "sqliteDB", databaseEngine = DatabaseEngine.SQLITE, entity = Purchase.class)
public interface PurchaseRepository {

    @SelectAll
    List<Customer> findAll();

    @DeleteById
    void deleteBydId(int id);

}
