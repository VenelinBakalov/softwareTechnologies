import java.util.Scanner;

public class IntToHexAndBin {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String numberStr = scan.nextLine();
        int number = Integer.parseInt(numberStr);

        String hexaNum = Integer.toString(number, 16).toUpperCase();
        String binaryNum = Integer.toString(number, 2);

        System.out.println(hexaNum);
        System.out.println(binaryNum);
    }
}
