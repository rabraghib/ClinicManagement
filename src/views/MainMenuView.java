package views;

import models.User;
import utils.ConsoleUtils;

public class MainMenuView {
    private final User currentUser;

    public MainMenuView() {
        this.currentUser = LoginView.getCurrentUser();
    }

    public void show() {
        while (true) {
            ConsoleUtils.clearScreen();
            ConsoleUtils.printTitle("Main Menu");

            System.out.println("\nWelcome, " + currentUser.getFullName());

            System.out.println("\nPlease select an option:");
            System.out.println("1. Clinic");
            System.out.println("2. Patients");
            System.out.println("3. Appointment & Schedule");
            System.out.println("4. Logout");
            System.out.println("5. Exit");

            int choice = ConsoleUtils.readInt("\nEnter your choice (1-5): ", 1, 5);

            switch (choice) {
                case 1 -> new ClinicView().show();
                case 2 -> new PatientView().show();
                case 3 -> new AppointmentView().show();
                case 4 -> {
                    LoginView.logout();
                    return;
                }
                case 5 -> {
                    System.out.println("\nThank you for using the Clinic Management System!");
                    System.exit(0);
                }
            }

            ConsoleUtils.waitForEnter();
        }
    }
}