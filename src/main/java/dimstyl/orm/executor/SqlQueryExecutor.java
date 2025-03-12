package dimstyl.orm.executor;

import dimstyl.orm.exceptions.SqlExecutionException;
import dimstyl.orm.utils.ConsoleUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public final class SqlQueryExecutor {

    private SqlQueryExecutor() {
    }

    public static void executeCreateTableQueries(final List<String> createTableQueries, final Connection connection)
            throws SqlExecutionException {
        ConsoleUtils.printFormatted("\n🔄️ Executing 'CREATE TABLE' queries\n");
        try {
            for (final String sqlQuery : createTableQueries) executeCreateTable(sqlQuery, connection);
        } catch (SQLException e) {
            ConsoleUtils.printFormatted("❌ Table(s) creation failed\n\tERROR: %s\n", e.getMessage());
            throw new SqlExecutionException(e.getMessage(), e);
        }

        ConsoleUtils.printFormatted("✅ Table(s) created successfully\n");
    }

    private static void executeCreateTable(final String sqlQuery, final Connection connection) throws SQLException {
        try (final Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlQuery);
        }
    }

}
