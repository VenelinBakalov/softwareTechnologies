import java.util.Scanner;


public class NumbersReversed {
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        String number = console.nextLine();
        PrintReversed(number);
    }

    private static void PrintReversed(String number) {
        for (int i=number.length() - 1; i >= 0; i--){
            System.out.print(number.charAt(i));
        }
        System.out.println();
    }
}
