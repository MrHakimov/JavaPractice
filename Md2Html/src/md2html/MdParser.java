package md2html;

import java.io.PrintWriter;
import java.util.*;

/**
 * @author: Muhammadjon Hakimov
 * created: 15.02.2019 16:01:57
 */

public class MdParser {
    private MdSource readSource;
    private PrintWriter writeSource;
    private int textEndPos;
    private int linkEndPos;
    private Map<String, Integer> assignments =  new HashMap<>();
    private Map <String, String> specialSymbols =  new HashMap<>();

    protected MdParser(FileMdSource readSource, PrintWriter writeSource) {
        this.readSource = readSource;
        this.writeSource = writeSource;
    }

    protected void parse() throws MdException {
        specialSymbols.put("<", "&lt;");
        specialSymbols.put(">", "&gt;");
        specialSymbols.put("&", "&amp;");
        readSource.nextChar();
        while (readSource.getChar() != MdSource.END) {
            skipEmptyLines();
            parseValue();
        }
    }

    private void parseValue() throws MdException {
        skipEmptyLines();
        int numberOfLattices = 0;
        while (readSource.getChar() == '#') {
            numberOfLattices++;
            readSource.nextChar();
        }

        if (readSource.getChar() == ' ' && numberOfLattices > 0) {
            writeSource.print("<h" + numberOfLattices + ">");
            readSource.nextChar();
            writeBlock(parseBlock());
            writeSource.print("</h" + numberOfLattices + ">\n");
        } else {
            writeSource.print("<p>");
            for (int i = 0; i < numberOfLattices; i++) {
                writeSource.print('#');
            }
            writeBlock(parseBlock());
            writeSource.print("</p>\n");
        }
    }

    private String parseBlock() throws MdException {
        StringBuilder block = new StringBuilder();
        readBlock(block);
        while (Character.isWhitespace(block.toString().charAt((block.toString().length()) - 1))) {
            block.deleteCharAt((block.toString().length()) - 1);
        }
        return block.toString();
    }

    private void readBlock(StringBuilder sb) throws MdException {
        do {
            sb.append(readSource.getChar());
            readSource.nextChar();
            boolean f = false;
            if (isNewLine()) {
                f = true;
                sb.append("\n");
                readSource.nextChar();
            }
            if (f && isNewLine()) {
                readSource.nextChar();
                break;
            }
        } while (readSource.getChar() != MdSource.END);
    }

    private void writeBlock(String block) throws MdException {
        assignments.clear();
        assignments.put("*", 0);
        assignments.put("_", 0);
        assignments.put("*1", 0);
        assignments.put("_1", 0);
        assignments.put("*2", 0);
        assignments.put("_2", 0);
        assignments.put("`", 0);
        assignments.put("\\", 0);
        assignments.put("-", 0);
        assignments.put("+", 0);
        assignments.put("~", 0);
        for (int i = 0; i < block.length(); i++) {
            String c = Character.toString(block.charAt(i));
            if (assignments.containsKey(c)) {
                switch (c) {
                    case "*":
                    case "_":
                        if (i + 1 < block.length() && Character.toString(block.charAt(i + 1)).equals(c)) {
                            i++;
                            assignments.put(c + "2", assignments.get(c + "2") + 1);
                        } else {
                            assignments.put(c + "1", assignments.get(c + "1") + 1);
                        }
                        break;
                    case "`":
                        assignments.put(c, assignments.get(c) + 1);
                        break;
                    case "\\":
                        i++;
                        break;
                    case "-":
                    case "+":
                        i++;
                        assignments.put(c, assignments.get(c) + 1);
                        break;
                    case "~":
                        assignments.put(c, assignments.get(c) + 1);
                        break;
                }
            }
        }
        for (Map.Entry<String, Integer> pair : assignments.entrySet()) {
            if (pair.getValue() % 2 == 1) {
                assignments.put(pair.getKey(), pair.getValue() - 1);
            }
        }

        int i = 0;
        while (i < block.length()) {
            i = implementAssignments(block, i);
        }
    }

    private int implementAssignments(String block, int index) throws MdException {
        String c = Character.toString(block.charAt(index));
        if (c.equals("*") || c.equals("_")) {
            if (index + 1 < block.length() && Character.toString(block.charAt(index + 1)).equals(c)
                    && assignments.get(c + "2") > 0) {
                if (assignments.get(c + "2") % 2 == 0) {
                    writeSource.print("<strong>");
                } else {
                    writeSource.print("</strong>");
                }
                index++;
                assignments.put(c + "2", assignments.get(c + "2") - 1);
            } else {
                if (assignments.get(c + "1") > 0) {
                    if (assignments.get(c + "1") % 2 == 0) {
                        writeSource.print("<em>");
                    } else {
                        writeSource.print("</em>");
                    }
                    assignments.put(c + "1", assignments.get(c + "1") - 1);
                } else {
                    writeSource.print(block.charAt(index));
                }
            }
        } else if (c.equals("`")) {
            if (assignments.get(c) > 0) {
                if (assignments.get(c) % 2 == 0) {
                    writeSource.print("<code>");
                } else {
                    writeSource.print("</code>");
                }
                assignments.put(c, assignments.get(c) - 1);
            }
        } else if (c.equals("-") || c.equals("+")) {
            if (index + 1 < block.length() && Character.toString(block.charAt(index + 1)).equals(c)
                    && assignments.get(c) > 0) {
                if (assignments.get(c) % 2 == 0) {
                    writeSource.print(c.equals("+") ? "<u>" : "<s>");
                } else {
                    writeSource.print(c.equals("+") ? "</u>" : "</s>");
                }
                index++;
                assignments.put(c, assignments.get(c) - 1);
            } else {
                writeSource.print(block.charAt(index));
            }
        } else if (c.equals("~")) {
            if (assignments.get(c) > 0) {
                if (assignments.get(c) % 2 == 0) {
                    writeSource.print("<mark>");
                } else {
                    writeSource.print("</mark>");
                }
                assignments.put(c, assignments.get(c) - 1);
            }
        } else if (c.equals("!")) {
            index = writeImageTag(block, index);
        } else if (c.equals("[")) {
            index =  writeLinkTag(block, index);
        } else if (specialSymbols.containsKey(c)) {
            writeSource.print(specialSymbols.get(c));
        } else if (!c.equals("\\")) {
            writeSource.print(block.charAt(index));
        }
        return index + 1;
    }

    private boolean checkImageOrLinkTag(String block, int index) throws MdException {
        textEndPos = -1;
        for (int i = index + 2; i < block.length(); i++) {
            if (block.charAt(i) == ']') {
                textEndPos = i;
                break;
            }
        }
        if (textEndPos == -1) {
            throw new MdException("Incorrect order of square brackets!");
        }
        if (textEndPos + 1 < block.length() && block.charAt(textEndPos + 1) != '(') {
            throw new MdException("Incorrect image or link tag! Expected \'(\', found: "
                    + block.charAt(textEndPos + 1) + "!");
        }
        linkEndPos = -1;
        for (int i = textEndPos + 2; i < block.length(); i++) {
            if (block.charAt(i) == ')') {
                linkEndPos = i;
                break;
            }
        }
        if (linkEndPos == -1) {
            throw new MdException("Incorrect image tag! Expected \')\', not found.");
        }
        return true;
    }

    private void writeLinkAddress(String block) {
        for (int i = textEndPos + 2; i < linkEndPos; i++) {
            writeSource.print(block.charAt(i));
        }
        writeSource.print("\'>");
    }

    private int writeLinkTag(String block, int index) throws MdException {
        if (!checkImageOrLinkTag(block, index - 1)) {
            writeSource.print("[");
            return index;
        }
        writeSource.print("<a href=\'");
        writeLinkAddress(block);
        int i = index + 1;
        while (i < textEndPos) {
            i = implementAssignments(block, i);
        }
        writeSource.print("</a>");
        return linkEndPos;
    }

    private int writeImageTag(String block, int index) throws MdException {
        if (index + 1 < block.length() && block.charAt(index + 1) != '[') {
            writeSource.print("!");
            return index;
        }
        checkImageOrLinkTag(block, index);
        writeSource.print("<img alt=\'");
        for (int i = index + 2; i < textEndPos; i++) {
            writeSource.print(block.charAt(i));
        }
        writeSource.print("\' src=\'");
        writeLinkAddress(block);
        return linkEndPos;
    }

    private void skipEmptyLines() throws MdException {
        while (isNewLine()) {
            readSource.nextChar();
        }
    }

    private boolean isNewLine() throws MdException {
        if (readSource.getChar() == '\r') {
            readSource.nextChar();
            return (readSource.getChar() == '\n');
        }
        return false;
    }
}
