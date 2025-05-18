package views;

import models.User;
import services.UserService;
import utils.ConsoleUtils;

public class LoginView {
    private static User currentUser;

    static User getCurrentUser() {
        return currentUser;
    }

    public static void ensureLoggedIn() {
        while (currentUser == null) {
            ConsoleUtils.clearScreen();
            ConsoleUtils.printTitle("Login");

            System.out.print("\tUsername: ");
            String username = ConsoleUtils.readLine();

            System.out.print("\tPassword: ");
            String password = ConsoleUtils.readLine();

            currentUser = UserService.login(username, password);

            if (currentUser == null) {
                System.out.println("\nInvalid username or password. Please try again.");
                ConsoleUtils.waitForEnter();
            }
        }
    }

    public static void logout() {
        currentUser = null;
    }
}