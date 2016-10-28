import java.util.Scanner;

public class CountSubstringOccurrences {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String text = scan.nextLine().toLowerCase();
        String pattern = scan.nextLine().toLowerCase();

        int counter = 0;
        int index = text.indexOf(pattern);

        while (index != -1){
            counter++;
            index = text.indexOf(pattern, index + 1);
        }

        System.out.println(counter);
    }
}
