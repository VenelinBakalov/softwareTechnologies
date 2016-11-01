import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * Created by Venelin on 1.11.2016 г..
 */
public class Phonebook {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] commandLine = scanner.nextLine().split(" ");

        LinkedHashMap<String, String> phonebook = new LinkedHashMap<>();

        while (!commandLine[0].equals("END")) {
            String command = commandLine[0];
            String name = commandLine[1];

            if (command.equals("A")) {
                String number = commandLine[2];
                phonebook.put(name, number);
            }
            else if (command.equals("S")) {
                if (phonebook.containsKey(name)) {
                    System.out.println(name + " -> " + phonebook.get(name));
                }
                else{
                    System.out.printf("Contact %s does not exist.", name);
                    System.out.println();
                }
            }
            commandLine = scanner.nextLine().split(" ");
        }
    }
}
