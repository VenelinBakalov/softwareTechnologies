import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Venelin on 29.10.2016 г..
 */
public class ExtractSentencesbyKeyword {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String word = scanner.nextLine();
        String text = scanner.nextLine();

        String pattern = "((" + word + "[^a-zA-Z0-9]|(\\w+[^a-zA-Z0-9!\\.\\?!]*[^a-zA-Z0-9]))+" + word + "([^a-zA-Z0-9]\\w+[^a-zA-Z0-9!\\.\\?!]*)+)(\\.|!|\\?)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(text);

        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }

    }
}
