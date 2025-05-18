package views;

import models.Clinic;
import models.User;
import services.ClinicService;
import utils.ConsoleUtils;

public class WelcomeView {
    public static void show() {
        ConsoleUtils.clearScreen();
        final Clinic clinic = ClinicService.getClinic();
        ConsoleUtils.printTitle("Welcome to " + clinic.name);

        final User currentUser = LoginView.getCurrentUser();
        if (currentUser != null) {
            System.out.println("Logged in as " + currentUser.email);
        }

        System.out.println("\nClinic Information");
        ConsoleUtils.printModelInfo(clinic.toKeyValueMap());
        ConsoleUtils.waitForEnter();
    }
}