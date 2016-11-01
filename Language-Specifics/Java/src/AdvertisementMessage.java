import java.util.Random;
import java.util.Scanner;

/**
 * Created by Venelin on 1.11.2016 г..
 */
public class AdvertisementMessage {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] phrases = new String[]{"Excellent product.", "Such a great product.", "I always use that product.",
                "Best product of its category.", "Exceptional product.", "I can’t live without this product."};
        String[] events = new String[]{"Now I feel good.", "I have succeeded with this product.",
                "Makes miracles.I am happy of the results!", "I cannot believe but now I feel awesome.",
                "Try it yourself, I am very satisfied.", "I feel great !"};
        String[] authors = new String[]{"Diana", "Petya", "Stella", "Elena", "Katya", "Iva", "Annie", "Eva"};
        String[] cities = new String[] {"Burgas", "Sofia", "Plovdiv", "Varna", "Ruse"};

        int count = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < count; i++) {
            Random random = new Random();

            String phrase = phrases[random.nextInt(count)];
            String event = events[random.nextInt(count)];
            String author = authors[random.nextInt(count)];
            String city = cities[random.nextInt(count)];

            System.out.println(String.format("%s %s %s - %s", phrase, event, author, city));
        }
    }
}
