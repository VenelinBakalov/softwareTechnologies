import java.util.Scanner;

public class EnglishNameOfLastDigit {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        String name = GetName(input);
        System.out.println(name);

    }

    private static String GetName(String input) {
        String lastDigit = input.substring(input.length() - 1);
        String name = "";
        switch (lastDigit){
            case "1": name = "one";
            break;
            case "2": name = "two";
            break;
            case "3": name = "three";
            break;
            case "4": name = "four";
            break;
            case "5": name = "five";
            break;
            case "6": name = "six";
            break;
            case "7": name = "seven";
            break;
            case "8": name = "eight";
            break;
            case "9": name = "nine";
            break;
            case "0": name = "zero";
            break;
        }
        return name;
    }
}
