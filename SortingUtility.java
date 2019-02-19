import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SortingUtility {
    private static List<String> linesWithNumbers = new ArrayList<>();
    private static List<String> linesWithLowerLetters = new ArrayList<>();
    private static List<String> otherLines = new ArrayList<>();
    private static Map<String, Integer> strToNum = new HashMap<>();
    private static Map<Integer, String> numToStr = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Введите ключевое слов \'sort\', выберите необходимую утилиту" +
                "\n\'-b\' для \'--ignore-leading-blanks\';" +
                "\n\'-d\' для \'--dictionary-order\';" +
                "\n\'-f\' для \'--ignore-case\';" +
                "\n\'-i\' для \'--ignore-nonprinting\';" +
                "\n\'-n\' для \'--numeric-sort\';" +
                "\n\'-r\' для \'--reverse\'" +
                "\n и введите имя файла.");
        Scanner sc = new Scanner(System.in);
        String[] input = sc.nextLine().split(" ");
        if (input.length == 2) {
            if (!input[0].equals("sort")) {
                System.err.println("Введена неправильная команда!");
            } else {
                try {
                    Scanner fileReader = new Scanner(new File(input[input.length - 1]));
                    int lineNumber = 1;
                    while (fileReader.hasNextLine()) {
                        String currentLine = fileReader.nextLine();
                        strToNum.put(currentLine, lineNumber);
                        numToStr.put(lineNumber, currentLine);
                        addLineToList(currentLine);
                        lineNumber++;
                    }
                    linesWithNumbers.sort(Comparator.naturalOrder());
                    linesWithLowerLetters.sort(Comparator.naturalOrder());
                    otherLines.sort(Comparator.naturalOrder());
                    showLists(linesWithNumbers, linesWithLowerLetters, otherLines);
                } catch (FileNotFoundException e) {
                    System.err.println("Ошибка при открытии файла!");
                }
            }
        } else if (input.length == 3) {
            String option = input[1];
            try {
                Scanner fileReader = new Scanner(new File(input[input.length - 1]));
                int numberOfLine = 1;
                boolean f = false;
                while (fileReader.hasNextLine()) {
                    String currentLine = fileReader.nextLine();
                    strToNum.put(currentLine, numberOfLine);
                    numToStr.put(numberOfLine, currentLine);
                    if (option.equals("-b")) {
                        if (!f && containsOfWhitespaces(currentLine)) {
                            numberOfLine++;
                            continue;
                        } else {
                            f = true;
                        }
                    }
                    else if (option.equals("-d")) {
                        StringBuilder modifiedLine = new StringBuilder();
                        for (int i = 0; i < currentLine.length(); i++) {
                            char c = currentLine.charAt(i);
                            if (Character.isWhitespace(c) || Character.isLetter(c) || Character.isDigit(c)) {
                                modifiedLine.append(c);
                            } else {
                                modifiedLine.append('\0');
                            }
                        }
                        currentLine = modifiedLine.toString();
                        strToNum.put(currentLine, numberOfLine);
                    } else if (option.equals("-f")) {
                        StringBuilder modifiedLine = new StringBuilder();
                        for (int i = 0; i < currentLine.length(); i++) {
                            char c = currentLine.charAt(i);
                            modifiedLine.append(Character.toLowerCase(c));
                        }
                        currentLine = modifiedLine.toString() + " " + numberOfLine;
                        strToNum.put(currentLine, numberOfLine);
                    } else if (option.equals("-i")) {
                        StringBuilder modifiedLine = new StringBuilder();
                        for (int i = 0; i < currentLine.length(); i++) {
                            char c = currentLine.charAt(i);
                            if ((int) c >= 33 && (int) c <= 126) {
                                modifiedLine.append(Character.toLowerCase(c));
                            } else {
                                modifiedLine.append('\0');
                            }
                        }
                        currentLine = modifiedLine.toString() + " " + numberOfLine;
                        strToNum.put(currentLine, numberOfLine);
                    } else if (option.equals("-n")) {
                        StringBuilder modifiedLine = new StringBuilder();
                        for (int i = 0; i < currentLine.length(); i++) {
                            char c = currentLine.charAt(i);
                            if (Character.isDigit(c)) {
                                modifiedLine.append(Character.toLowerCase(c));
                            } else {
                                modifiedLine.append('\0');
                            }
                        }
                        currentLine = modifiedLine.toString() + " " + numberOfLine;
                        strToNum.put(currentLine, numberOfLine);
                    }
                    addLineToList(currentLine);
                    numberOfLine++;
                }
                linesWithNumbers.sort(Comparator.naturalOrder());
                linesWithLowerLetters.sort(Comparator.naturalOrder());
                otherLines.sort(Comparator.naturalOrder());
                if (!option.equals("-r")) {
                    showLists(linesWithNumbers, linesWithLowerLetters, otherLines);
                } else {
                    showLists(otherLines, linesWithLowerLetters, linesWithNumbers);
                }
            } catch (FileNotFoundException e) {
                System.err.println("Ошибка при открытии файла!");
            }
        } else {
            System.err.println("Данные введены некорректно!");
        }
    }

    private static boolean containsOfWhitespaces(String currentLine) {
        for (int i = 0; i < currentLine.length(); i++) {
            if (!Character.isWhitespace(currentLine.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static void addLineToList(String currentLine) {
        boolean digit = false;
        for (int i = 0; i < currentLine.length(); i++) {
            if (Character.isDigit(currentLine.charAt(i))) {
                digit = true;
                break;
            }
        }
        if (digit) {
            linesWithNumbers.add(currentLine);
            return;
        }
        if (currentLine.length() > 0 && Character.toLowerCase(currentLine.charAt(0)) == currentLine.charAt(0)) {
            linesWithLowerLetters.add(currentLine);
            return;
        }
        otherLines.add(currentLine);
    }

    private static void showLists(List<String> linesWithNumbers, List<String> linesWithLowerLetters, List<String> otherLines) {
        for (String line : linesWithNumbers) {
            System.out.println(numToStr.get(strToNum.get(line)));
        }
        for (String line : linesWithLowerLetters) {
            System.out.println(numToStr.get(strToNum.get(line)));
        }
        for (String line : otherLines) {
            System.out.println(numToStr.get(strToNum.get(line)));
        }
    }
}
