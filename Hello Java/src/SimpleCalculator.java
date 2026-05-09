import java.util.Scanner;

class OutOfRangeException extends Exception {
    public OutOfRangeException() {
        super("OutOfRangeException");
    }
}
class AddZeroException extends Exception {
    public AddZeroException() {
        super("AddZeroException");
    }
}
class SubtractZeroException extends Exception {
    public SubtractZeroException() {
        super("SubtractZeroException");
    }
}
public class SimpleCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();
        try {
            int opIndex = input.indexOf('+');
            char operator = '+';
            if (opIndex == -1) {
                opIndex = input.indexOf('-');
                operator = '-';
            }
            String strA = input.substring(0, opIndex);
            String strB = input.substring(opIndex + 1);
            int a = Integer.parseInt(strA);
            int b = Integer.parseInt(strB);
            if (operator == '+') {
                if (a == 0 || b == 0) {
                    throw new AddZeroException();
                }
            } else if (operator == '-') {
                if (a == 0 || b == 0) {
                    throw new SubtractZeroException();
                }
            }
            if (a < 0 || a > 1000 || b < 0 || b > 1000) {
                throw new OutOfRangeException();
            }
            int result = (operator == '+') ? (a + b) : (a - b);
            if (result < 0 || result > 1000) {
                throw new OutOfRangeException();
            }
            System.out.println(result);
        } catch (AddZeroException | SubtractZeroException | OutOfRangeException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input");
        }
    }
}