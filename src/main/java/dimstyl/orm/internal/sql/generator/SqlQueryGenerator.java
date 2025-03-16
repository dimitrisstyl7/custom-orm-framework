package dimstyl.orm.internal.sql.generator;

import dimstyl.orm.metadata.Metadata;

/**
 * A generic interface for generating SQL queries based on metadata.
 *
 * <p>This interface defines a contract for generating SQL queries using
 * specific metadata.
 * Implementations of this interface generate SQL queries
 * for various database operations such as `SELECT`, and `DELETE`.</p>
 *
 * @param <K> The type of the result produced by the query generator
 *            (e.g., {@code String}, {@code Optional<String>}, {@code List<String>}).
 * @param <T> The type of metadata used to generate the query. It must extend {@link Metadata}.
 */
public interface SqlQueryGenerator<K, T extends Metadata> {

    /**
     * Generates an SQL query based on the provided metadata.
     *
     * @param metadata The metadata used to generate the SQL query.
     * @return The generated SQL query or query-related result.
     */
    K generate(final T metadata);

}
