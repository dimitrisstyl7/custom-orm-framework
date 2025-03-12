package dimstyl.orm.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public final class FileUtils {

    private FileUtils() {
    }

    public static String writeToFileAndGetAbsolutePath(String fileName, String content) throws IOException {
        try (final FileWriter fileWriter = new FileWriter(fileName)) {
            final PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(content);
        }
        File file = new File(fileName);
        return file.getAbsolutePath();
    }

}
