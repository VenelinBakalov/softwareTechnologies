import java.util.Scanner;

public class PrimeChecker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long number = Long.parseLong(scanner.nextLine());

        boolean isPrime = IsPrime(number);
        if (isPrime){
            System.out.println("True");
        }
        else{
            System.out.println("False");
        }
    }

    private static boolean IsPrime(long number) {
        if (number <= 1){
            return false;
        }

        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0){
                return false;
            }
        }

        return true;
    }
}
