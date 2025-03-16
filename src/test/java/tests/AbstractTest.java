package tests;

import org.junit.jupiter.api.DisplayNameGeneration;

/**
 * Custom display name generator that replaces camel case with spaces for better readability in test reports.
 */
@DisplayNameGeneration(CustomDisplayNameGenerator.ReplaceCamelCase.class)
abstract class AbstractTest {
}
