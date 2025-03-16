package dimstyl.orm.internal.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Utility class for file operations.
 *
 * <p>This class provides a method to write content to a file and return its absolute path.</p>
 *
 * <p><strong>Usage Example:</strong></p>
 * <pre>
 *     try {
 *         String path = FileUtils.writeToFileAndGetAbsolutePath("output.txt", "Hello, World!");
 *         System.out.println("File saved at: " + path);
 *     } catch (IOException e) {
 *         e.printStackTrace();
 *     }
 * </pre>
 *
 * <p><strong>Design Notes:</strong></p>
 * <ul>
 *     <li>This class is {@code final} to prevent inheritance.</li>
 *     <li>The constructor is private to enforce a static utility pattern.</li>
 * </ul>
 */
public final class FileUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private FileUtils() {
    }

    /**
     * Writes the given content to a file and returns its absolute path.
     *
     * @param fileName The name (or relative path) of the file to write.
     * @param content  The content to be written to the file.
     * @return The absolute path of the created file.
     * @throws IOException If an I/O error occurs during file writing.
     */
    public static String writeToFileAndGetAbsolutePath(String fileName, String content) throws IOException {
        try (final FileWriter fileWriter = new FileWriter(fileName)) {
            final PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(content);
        }
        File file = new File(fileName);
        return file.getAbsolutePath();
    }

}
