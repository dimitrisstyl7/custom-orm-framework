package dimstyl.orm.internal.sql.execution;

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

public final class SqlQueryExecutor {

    private static final Map<Class<?>, Class<?>> primitiveToWrapperMap = Map.ofEntries(
            Map.entry(boolean.class, Boolean.class),
            Map.entry(short.class, Short.class),
            Map.entry(int.class, Integer.class),
            Map.entry(long.class, Long.class),
            Map.entry(float.class, Float.class),
            Map.entry(double.class, Double.class)
    );

    private SqlQueryExecutor() {
    }

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

    private static void executeCreateTable(final String query, final Connection connection) throws SQLException {
        try (final Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }

}
