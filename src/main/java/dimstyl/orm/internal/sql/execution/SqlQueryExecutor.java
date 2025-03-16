package dimstyl.orm.internal.sql.execution;

import dimstyl.orm.annotations.Column;
import dimstyl.orm.exceptions.MissingColumnAnnotationException;
import dimstyl.orm.exceptions.SqlExecutionException;
import dimstyl.orm.internal.processors.TableProcessor;
import dimstyl.orm.internal.utils.ConsoleUtils;
import dimstyl.orm.model.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility class responsible for executing SQL queries related to ORM operations.
 * <p>
 * This class provides methods for executing SQL queries such as `CREATE TABLE`, `SELECT ALL`,
 * and `DELETE BY ID` while mapping results to entity classes dynamically.
 * </p>
 */
public final class SqlQueryExecutor {

    /**
     * A mapping of primitive types to their corresponding wrapper classes.
     * <p>
     * This is useful when retrieving values from a {@code ResultSet}, as JDBC
     * returns wrapper types instead of primitives.
     * </p>
     */
    private static final Map<Class<?>, Class<?>> primitiveToWrapperMap = Map.ofEntries(
            Map.entry(boolean.class, Boolean.class),
            Map.entry(short.class, Short.class),
            Map.entry(int.class, Integer.class),
            Map.entry(long.class, Long.class),
            Map.entry(float.class, Float.class),
            Map.entry(double.class, Double.class)
    );

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private SqlQueryExecutor() {
    }

    /**
     * Executes a list of `CREATE TABLE` SQL queries.
     *
     * @param createTableQueries List of SQL `CREATE TABLE` queries.
     * @param connection         The database connection.
     * @throws SqlExecutionException If an error occurs while executing the queries.
     */
    public static void executeCreateTableQueries(final List<String> createTableQueries, final Connection connection)
            throws SqlExecutionException {
        ConsoleUtils.printFormatted("\n🔄️ Executing 'CREATE TABLE' queries\n");
        try {
            for (final String query : createTableQueries) executeCreateTable(query, connection);
        } catch (SQLException e) {
            ConsoleUtils.printFormatted("❌ Table(s) creation failed\n\tERROR: %s\n", e.getMessage());
            throw new SqlExecutionException(e.getMessage(), e);
        }
        ConsoleUtils.printFormatted("✅ Table(s) created successfully\n");
    }

    /**
     * Executes a `SELECT ALL` query and maps the results to a list of entity objects.
     *
     * @param query       The SQL `SELECT ALL` query to be executed.
     * @param connection  The database connection.
     * @param entityClass The entity class type to map the result set.
     * @param <T>         The type of the entity extending {@link Entity}.
     * @return A list of mapped entity objects.
     * @throws MissingColumnAnnotationException If a required {@link Column} annotation is missing.
     * @throws SqlExecutionException            If an error occurs during query execution.
     */
    public static <T extends Entity> List<T> executeSelectAllQuery(final String query,
                                                                   final Connection connection,
                                                                   final Class<T> entityClass)
            throws MissingColumnAnnotationException, SqlExecutionException {
        ConsoleUtils.printFormatted("\n🔄️ Executing 'SELECT ALL' query\n");
        final List<T> resultList = new ArrayList<>();

        try (final Statement statement = connection.createStatement()) {
            // Map<String, String>: key -> entity class field name, value -> table column name
            final Map<String, String> fieldToColumnMap = TableProcessor.mapFieldsToColumns(entityClass);

            // Get the no-arg constructor
            final Constructor<?> constructor = entityClass.getDeclaredConstructor();
            constructor.setAccessible(true); // Allow access to private constructors if needed

            final var resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                // Create a new instance of the entity
                final T entity = (T) constructor.newInstance();

                // Set field values dynamically
                for (final Map.Entry<String, String> entry : fieldToColumnMap.entrySet()) {
                    final String column = entry.getValue();
                    final Field field = entityClass.getDeclaredField(entry.getKey());
                    final Class<?> fieldType = field.getType().isPrimitive() ?
                            primitiveToWrapperMap.get(field.getType()) : field.getType();
                    final var value = resultSet.getObject(column, fieldType);
                    field.setAccessible(true);
                    field.set(entity, value);
                }
                resultList.add(entity);
            }
        } catch (SQLException | NoSuchMethodException | NoSuchFieldException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            ConsoleUtils.printFormatted("❌ 'SELECT ALL' query failed\n\tERROR: %s\n", e.getMessage());
            throw new SqlExecutionException(e.getMessage(), e);
        }
        ConsoleUtils.printFormatted("✅ 'SELECT ALL' query executed successfully\n");
        return resultList;
    }

    /**
     * Executes a `DELETE BY ID` query to remove a record from the database.
     *
     * @param query      The SQL `DELETE` query with a placeholder for the ID.
     * @param connection The database connection.
     * @param id         The ID value to use in the query.
     * @param <T>        The type of the ID (e.g., Integer, Long, String).
     * @throws SqlExecutionException If an error occurs while executing the query.
     */
    public static <T> void executeDeleteByIdQuery(final String query, final Connection connection, final T id)
            throws SqlExecutionException {
        ConsoleUtils.printFormatted("\n🔄️ Executing 'DELETE BY ID' query\n");
        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            ConsoleUtils.printFormatted("❌ 'DELETE BY ID' query failed\n\tERROR: %s\n", e.getMessage());
            throw new SqlExecutionException(e.getMessage(), e);
        }
        ConsoleUtils.printFormatted("✅ 'DELETE BY ID' query executed successfully\n");
    }

    /**
     * Executes a single `CREATE TABLE` query.
     *
     * @param query      The `CREATE TABLE` SQL query.
     * @param connection The database connection.
     * @throws SQLException If an error occurs during query execution.
     */
    private static void executeCreateTable(final String query, final Connection connection) throws SQLException {
        try (final Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }

}
