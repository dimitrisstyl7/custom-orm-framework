package dimstyl.orm.resolvers;

import dimstyl.orm.exceptions.UnsupportedFieldTypeException;

import java.lang.reflect.Field;
import java.util.Map;

enum DerbyColumnTypeResolver implements ColumnTypeResolver {

    INSTANCE;

    // TODO: add link
    private final Map<Class<?>, String> TYPE_MAP = Map.ofEntries(
            // TODO: implement
    );

    @Override
    public String resolve(final Field field) throws UnsupportedFieldTypeException {
        return ""; // TODO: implement
    }
}
