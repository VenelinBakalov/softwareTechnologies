import java.util.Scanner;


public class VarHexFormat {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String hexaStr = scanner.nextLine().substring(2);
        int hexa = Integer.parseInt(hexaStr, 16);

        System.out.println(hexa);
    }
}
