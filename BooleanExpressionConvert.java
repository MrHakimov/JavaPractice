import java.util.Scanner;

public class BooleanExpressionConvert {
    private static StringBuilder output = new StringBuilder();

    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);
        if (!inputScanner.hasNextLine()) {
            System.err.println("Input data is entered wrong or not entered!");
        }

        String input = inputScanner.nextLine();
        StringBuilder sb = new StringBuilder();
        int balance = 0;
        boolean expressionIsNotCorrect = false;
        for (int i = 0; i < input.length(); i++) {
            if (Character.isWhitespace(input.charAt(i))) {
                continue;
            }
            if (input.charAt(i) == '(') {
                balance++;
            } else if (input.charAt(i) == ')') {
                balance--;
                if (balance < 0 || (i == input.length() - 1 && balance != 0)) {
                    expressionIsNotCorrect = true;
                }
            }
            sb.append(input.charAt(i));
        }

        String expression = sb.toString();
        int exprLength = expression.length();
        if (expressionIsNotCorrect) {
            System.err.println("The number of opening brackets is not equal to number of closing brackets!");
        } else if (exprLength == 0) {
            System.err.println("Nothing is given in input!");
        } else if (exprLength == 1) {
            if (Character.isLetter(expression.charAt(0)) || expression.charAt(0) == '1'
                    || expression.charAt(0) == '0') {
                System.err.println(expression);
            } else {
                System.err.println("Incorrect symbol is entered!");
            }
        } else {
            int i = 0;
            while (i < exprLength) {
                if (expression.charAt(i) == '~') {
                    i++;
                    if (i < exprLength && expression.charAt(i) == '(') {
                        i = findLeftBracket(expression, i);
                        if (i == -1) {
                            printError();
                        }
                    } else if (isIdentifierOrOperation(expression, i)) {
                        Inverse(expression, i);
                    } else {
                        printError();
                    }
                } else if (isCorrectSymbol(expression, i)) {
                    output.append(expression.charAt(i));
                } else {
                    printError();
                }
                i++;
            }
            for (int j = 0; j < output.length(); j++) {
                System.out.print(output.charAt(j) + " ");
            }
        }

    }

    private static boolean isIdentifierOrOperation(String expression, int index) {
        return Character.isLetter((expression.charAt(index))) || expression.charAt(index) == '1' ||
                expression.charAt(index) == '0' || expression.charAt(index) == '|' || expression.charAt(index) == '&';
    }

    private static boolean isCorrectSymbol(String expression, int index) {
        return Character.isLetter((expression.charAt(index))) || expression.charAt(index) == '1'
                || expression.charAt(index) == '0' || expression.charAt(index) == '|'
                || expression.charAt(index) == '&' || expression.charAt(index) == '('
                || expression.charAt(index) == ')';
    }

    private static int findLeftBracket(String expression, int left) {
        for (int i = left + 1; i < expression.length(); i++) {
            if (expression.charAt(i) == ')') {
                deMorganFormula(expression, left + 1, i);
                return i;
            }
        }
        return -1;
    }

    private static void Inverse(String expression, int i) {
        if (Character.isLetter((expression.charAt(i)))) {
            output.append("~");
            output.append(expression.charAt(i));
        } else if (expression.charAt(i) == '1') {
            output.append('0');
        } else if (expression.charAt(i) == '&') {
            output.append('|');
        } else if (expression.charAt(i) == '0') {
            output.append('1');
        } else if (expression.charAt(i) == '|') {
            output.append('&');
        } else {
            printError();
        }
    }

    private static void deMorganFormula(String expression, int L, int R) {
        while (L < R) {
            if (expression.charAt(L) == '~') {
                L++;
                if (expression.charAt(L) == '(') {
                    L = findLeftBracket(expression, L);
                    if (L == -1) {
                        printError();
                    }
                } else if (isCorrectSymbol(expression, L)) {
                    Inverse(expression, L);
                    L++;
                } else {
                    printError();
                }
            } else if (isIdentifierOrOperation(expression, L)) {
                Inverse(expression, L);
                L++;
            } else {
                printError();
            }
        }
    }

    private static void printError() {
        System.err.println("Element has an incompatible type!");
        System.exit(1);
    }
}
