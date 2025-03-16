package dimstyl.orm.internal.processors;

import dimstyl.orm.annotations.DeleteById;
import dimstyl.orm.annotations.Repository;
import dimstyl.orm.annotations.SelectAll;
import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.enums.SqlOperation;
import dimstyl.orm.exceptions.MissingRepositoryAnnotationException;
import dimstyl.orm.internal.utils.ConsoleUtils;
import dimstyl.orm.metadata.RepositoryMetadata;
import dimstyl.orm.model.Entity;

import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Set;

/**
 * Utility class responsible for processing metadata related to repositories in ORM.
 * <p>
 * This class extracts metadata from repository classes annotated with {@link Repository},
 * identifying the database, entity, supported SQL operations, and table name.
 * </p>
 */
public final class RepositoryProcessor {

    /**
     * Private constructor to prevent instantiation.
     */
    private RepositoryProcessor() {
    }

    /**
     * Extracts metadata from a given class annotated with {@link Repository}.
     * <p>
     * This method validates the presence of the {@link Repository} annotation,
     * retrieves the database name, database engine, and associated entity.
     * It also scans for methods annotated with {@link SelectAll} and {@link DeleteById}
     * to determine the repository's supported SQL operations.
     * </p>
     *
     * @param repositoryClass The class representing the repository.
     * @return The extracted {@link RepositoryMetadata}, containing database details, entity, table name, and supported operations.
     * @throws MissingRepositoryAnnotationException If the class is not annotated with {@link Repository}.
     */
    public static RepositoryMetadata extractMetadata(final Class<?> repositoryClass)
            throws MissingRepositoryAnnotationException {
        final String repositoryClassName = repositoryClass.getSimpleName();
        ConsoleUtils.printFormatted("\n🔄️ Processing repository '%s'...\n", repositoryClassName);

        // If the @Repository annotation does not exist, throw MissingRepositoryAnnotationException
        if (!repositoryClass.isAnnotationPresent(Repository.class)) {
            final String message = String.format("Missing @Repository annotation in class '%s'", repositoryClassName);
            throw new MissingRepositoryAnnotationException(message);
        }

        final Repository repositoryAnnotation = repositoryClass.getDeclaredAnnotation(Repository.class);
        final String databaseName = repositoryAnnotation.databaseName();
        final DatabaseEngine databaseEngine = repositoryAnnotation.databaseEngine();
        final String tableName = TableProcessor.resolveTableName(repositoryAnnotation.entity());
        final Class<? extends Entity> entityClass = repositoryAnnotation.entity();
        Set<SqlOperation> supportedOperations = EnumSet.noneOf(SqlOperation.class);

        for (final Method method : repositoryClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(SelectAll.class)) supportedOperations.add(SqlOperation.SELECT_ALL);
            if (method.isAnnotationPresent(DeleteById.class)) supportedOperations.add(SqlOperation.DELETE_BY_ID);
        }

        ConsoleUtils.printFormatted("✅ Repository processed successfully\n");

        return RepositoryMetadata.builder()
                .databaseName(databaseName)
                .databaseEngine(databaseEngine)
                .tableName(tableName)
                .entityClass(entityClass)
                .supportedOperations(supportedOperations)
                .build();
    }

}
