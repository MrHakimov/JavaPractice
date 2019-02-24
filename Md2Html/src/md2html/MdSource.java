package md2html;

import java.io.IOException;

/**
 * @author: Muhammadjon Hakimov
 * created: 15.02.2019 16:02:57
 */

public abstract class MdSource {
    protected static char END = '\0';
    protected int pos = 0;
    protected int line = 1;
    protected int posInLine = 0;
    private char c;

    protected abstract char readChar() throws IOException;

    protected char getChar() {
        return c;
    }

    protected void nextChar() throws MdException {
        try {
            if (c == '\n') {
                line++;
                posInLine = 0;
            }
            c = readChar();
            pos++;
            posInLine++;
        } catch (final IOException e) {
            throw error("Source read error", e.getMessage());
        }
    }

    protected MdException error(final String format, final Object... args) {
        return new MdException(String.format("%d:%d: %s", line, posInLine, String.format(format, args)));
    }
}
