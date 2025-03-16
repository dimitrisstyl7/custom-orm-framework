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

public final class RepositoryProcessor {

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
