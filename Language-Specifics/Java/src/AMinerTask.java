import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Venelin on 1.11.2016 г..
 */
public class AMinerTask {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String resource = scanner.nextLine();

        LinkedHashMap<String, Long> resources = new LinkedHashMap<>();

        while (!resource.equals("stop")) {
            long quantity = Integer.parseInt(scanner.nextLine());

            if (!resources.containsKey(resource)) {
                resources.put(resource, quantity);
            }
            else {
                resources.put(resource, resources.get(resource) + quantity);
            }
            resource = scanner.nextLine();
        }

        for (Map.Entry<String, Long> resourceInfo : resources.entrySet()) {
            System.out.println(resourceInfo.getKey() + " -> " + resourceInfo.getValue());
        }
    }
}
