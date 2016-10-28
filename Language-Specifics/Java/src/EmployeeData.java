import java.util.Scanner;

/**
 * Created by Dani on 26.10.2016 г..
 */
public class EmployeeData {
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        String firstName = console.nextLine();
        String lastName = console.nextLine();
        String ageStr = console.nextLine();
        int age = Integer.parseInt(ageStr);
        String genderStr = console.nextLine();
        char gender = genderStr.charAt(0);
        String personalIdStr = console.nextLine();
        long personalId = Long.parseLong(personalIdStr);
        String employeeNumberStr = console.nextLine();
        long employeeNumber = Long.parseLong(employeeNumberStr);

        System.out.printf("First name: %s", firstName);
        System.out.println();
        System.out.println("Last name: " + lastName);
        System.out.println("Age: " + age);
        System.out.printf("Gender: %c", gender);
        System.out.println();
        System.out.println("Personal ID: " + personalId);
        System.out.println("Unique Employee number: " + employeeNumber);
    }
}
