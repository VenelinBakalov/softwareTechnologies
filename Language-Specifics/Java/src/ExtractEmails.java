import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Venelin on 29.10.2016 г..
 */
public class ExtractEmails {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String regex = "(\\s|^)([a-zA-Z0-9]+([\\.|\\-|_][a-zA-Z0-9]+)*@[a-zA-Z]+(\\-[a-zA-Z]+)*(\\.[a-zA-Z]+(\\-[a-zA-Z]+)*)+)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            System.out.println(matcher.group(2));
        }
        //(\s|^)([a-zA-Z0-9]+([\.|\-|_][a-zA-Z0-9]+)*@[a-zA-Z]+(\-[a-zA-Z]+)*(\.[a-zA-Z]+(\-[a-zA-Z]+)*)+)
    }
}
