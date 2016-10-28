import java.util.Arrays;
import java.util.Scanner;

public class TextFilter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] banWords = scanner.nextLine().split("[, ]");
        String input = scanner.nextLine();

        for (String word:banWords) {
            String replacer = getStringWithLengthAndFilledWithCharacter(word.length(), '*');
            input = input.replace(word, replacer);
        }

        System.out.println(input);
    }

    private static String getStringWithLengthAndFilledWithCharacter(int length, char c) {
        if (length > 0){
            char[] array = new char[length];
            Arrays.fill(array, c);
            return new String(array);
        }
        return "";
    }


}