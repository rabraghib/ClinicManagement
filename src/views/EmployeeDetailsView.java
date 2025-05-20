package views;

import models.Doctor;
import models.User;
import models.Assistant;
import services.UserService;
import utils.ConsoleUtils;

public class EmployeeDetailsView {
    private final User employee;

    public EmployeeDetailsView(User employee) {
        this.employee = employee;
    }

    public void show() {
        while (true) {
            ConsoleUtils.clearScreen();
            ConsoleUtils.printTitle("Employee Details");

            ConsoleUtils.printModelInfo(employee.toKeyValueMap());

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
            editDoctor(doctor);
        } else if (employee instanceof Assistant assistant) {
            editAssistant(assistant);
        }
    }

    private void editDoctor(Doctor doctor) {
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

        System.out.print("Specialty [" + doctor.specialty + "]: ");
        String specialty = ConsoleUtils.readLine();
        if (!specialty.isEmpty())
            doctor.specialty = specialty;

        UserService.saveDoctor(doctor);
        System.out.println("\nDoctor updated successfully!");
        ConsoleUtils.waitForEnter();
    }

    private void editAssistant(Assistant assistant) {
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

        UserService.saveAssistant(assistant);
        System.out.println("\nAssistant updated successfully!");
        ConsoleUtils.waitForEnter();
    }

    private boolean removeEmployee() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Remove Employee");

        ConsoleUtils.printModelInfo(employee.toKeyValueMap());

        System.out.println();
        boolean confirm = ConsoleUtils.readBool("Are you sure you want to remove this employee?");
        if (!confirm) {
            return false;
        }

        if (employee instanceof Doctor doctor) {
            UserService.removeDoctor(doctor.id);
        } else if (employee instanceof Assistant assistant) {
            UserService.removeAssistant(assistant.id);
        }

        System.out.println("\nEmployee removed successfully!");
        ConsoleUtils.waitForEnter();
        return true;
    }
}