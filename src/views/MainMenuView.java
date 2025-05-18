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
            System.out.println("4. Prescription");
            System.out.println("5. Logout");
            System.out.println("6. Exit");
            System.out.println("\n" + "=".repeat(50));

            int choice = ConsoleUtils.readInt("Enter your choice (1-6): ", 1, 6);

            switch (choice) {
                case 1:
                    new ClinicView().show();
                    break;
                case 2:
                    new PatientView().show();
                    break;
                case 3:
                    new AppointmentView().show();
                    break;
                case 4:
                    new PrescriptionView().show();
                    break;
                case 5:
                    System.out.println("\nLogging out...");
                    LoginView.logout();
                    return;
                case 6:
                    System.out.println("\nThank you for using the Clinic Management System!");
                    System.exit(0);
            }

            ConsoleUtils.waitForEnter();
        }
    }
}