package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author: Muhammadjon Hakimov
 * created: 15.02.2019 16:21:38
 */

public class FileMdSource extends MdSource {
    private final Reader reader;

    protected FileMdSource(final String fileName) throws MdException {
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
        } catch (final IOException e) {
            throw error("Error opening input file '%s': %s", fileName, e.getMessage());
        }
    }

    @Override
    protected char readChar() throws IOException {
        final int read = reader.read();
        return read == -1 ? END : (char) read;
    }

    protected void close() throws IOException {
        reader.close();
    }
}
