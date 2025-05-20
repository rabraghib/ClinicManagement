package views;

import models.Doctor;
import models.Assistant;
import services.UserService;
import utils.ConsoleUtils;

public class EmployeeDetailsView {
    private final Object employee;

    public EmployeeDetailsView(Object employee) {
        this.employee = employee;
    }

    public void show() {
        while (true) {
            ConsoleUtils.clearScreen();
            ConsoleUtils.printTitle("Employee Details");

            if (employee instanceof Doctor doctor) {
                ConsoleUtils.printModelInfo(doctor.toKeyValueMap());
            } else if (employee instanceof Assistant assistant) {
                ConsoleUtils.printModelInfo(assistant.toKeyValueMap());
            }

            System.out.println("\nPlease select an option:");
            System.out.println("1. Edit");
            System.out.println("2. Remove");
            System.out.println("3. Back");

            int choice = ConsoleUtils.readInt("\nEnter your choice (1-3): ", 1, 3);

            switch (choice) {
                case 1 -> editEmployee();
                case 2 -> {
                    if (removeEmployee()) {
                        return;
                    }
                }
                case 3 -> {
                    return;
                }
            }
        }
    }

    private void editEmployee() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Edit Employee");

        if (employee instanceof Doctor doctor) {
            System.out.println("\nEnter new information (press Enter to keep current value):");

            System.out.print("First Name [" + doctor.firstName + "]: ");
            String firstName = ConsoleUtils.readLine();
            if (!firstName.isEmpty())
                doctor.firstName = firstName;

            System.out.print("Last Name [" + doctor.lastName + "]: ");
            String lastName = ConsoleUtils.readLine();
            if (!lastName.isEmpty())
                doctor.lastName = lastName;

            System.out.print("Email [" + doctor.email + "]: ");
            String email = ConsoleUtils.readLine();
            if (!email.isEmpty())
                doctor.email = email;

            System.out.print("Username [" + doctor.username + "]: ");
            String username = ConsoleUtils.readLine();
            if (!username.isEmpty())
                doctor.username = username;

            System.out.print("Specialty [" + doctor.specialty + "]: ");
            String specialty = ConsoleUtils.readLine();
            if (!specialty.isEmpty())
                doctor.specialty = specialty;

            UserService.saveDoctor(doctor);
            System.out.println("\nDoctor updated successfully!");
        } else if (employee instanceof Assistant assistant) {
            System.out.println("\nEnter new information (press Enter to keep current value):");

            System.out.print("First Name [" + assistant.firstName + "]: ");
            String firstName = ConsoleUtils.readLine();
            if (!firstName.isEmpty())
                assistant.firstName = firstName;

            System.out.print("Last Name [" + assistant.lastName + "]: ");
            String lastName = ConsoleUtils.readLine();
            if (!lastName.isEmpty())
                assistant.lastName = lastName;

            System.out.print("Email [" + assistant.email + "]: ");
            String email = ConsoleUtils.readLine();
            if (!email.isEmpty())
                assistant.email = email;

            System.out.print("Username [" + assistant.username + "]: ");
            String username = ConsoleUtils.readLine();
            if (!username.isEmpty())
                assistant.username = username;

            UserService.saveAssistant(assistant);
            System.out.println("\nAssistant updated successfully!");
        }

        ConsoleUtils.waitForEnter();
    }

    private boolean removeEmployee() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Remove Employee");

        System.out.println("Are you sure you want to remove this employee? (y/n)");
        String confirm = ConsoleUtils.readLine().toLowerCase();
        if (!confirm.equals("y")) {
            return false;
        }

        if (employee instanceof Doctor doctor) {
            UserService.removeDoctor(doctor.id);
            System.out.println("\nDoctor removed successfully!");
        } else if (employee instanceof Assistant assistant) {
            UserService.removeAssistant(assistant.id);
            System.out.println("\nAssistant removed successfully!");
        }

        ConsoleUtils.waitForEnter();
        return true;
    }
} 