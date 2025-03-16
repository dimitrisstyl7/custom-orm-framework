package dimstyl.orm.configuration;

import dimstyl.orm.enums.SqlOperation;
import dimstyl.orm.exceptions.DatabaseConnectionException;

import java.sql.Connection;

public interface DatabaseConfiguration extends AutoCloseable {

    void connect(final String databaseName, final SqlOperation sqlOperation) throws DatabaseConnectionException;

    @Override
    void close() throws DatabaseConnectionException;

    Connection getConnection() throws DatabaseConnectionException;

}
