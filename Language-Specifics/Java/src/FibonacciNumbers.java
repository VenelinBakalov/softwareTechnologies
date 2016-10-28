import java.util.Scanner;

public class FibonacciNumbers {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int number = Integer.parseInt(scanner.nextLine());

        int fibNumber = GetFibonacciNumber(number);
        System.out.println(fibNumber);
    }

    private static int GetFibonacciNumber(int number) {
        if (number < 2){
            return 1;
        }
        int firstNumber = 1;
        int secondNumber = 1;

        for (int i = 2; i <= number; i++){
            int thirdNumber = firstNumber + secondNumber;
            firstNumber = secondNumber;
            secondNumber = thirdNumber;
        }

        return secondNumber;

    }
}
