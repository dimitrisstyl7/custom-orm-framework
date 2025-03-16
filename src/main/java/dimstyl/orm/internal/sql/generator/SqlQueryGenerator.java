package dimstyl.orm.internal.sql.generator;

import dimstyl.orm.metadata.Metadata;

public interface SqlQueryGenerator<K, T extends Metadata> {

    K generate(final T metadata);

}
