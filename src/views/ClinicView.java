package views;

import models.Clinic;
import models.Doctor;
import models.Assistant;
import services.ClinicService;
import services.UserService;
import utils.ConsoleUtils;

import java.util.List;

public class ClinicView {

    public void show() {
        while (true) {
            ConsoleUtils.clearScreen();
            ConsoleUtils.printTitle("Clinic Management");

            final Clinic clinic = ClinicService.get();
            ConsoleUtils.printModelInfo(clinic.toKeyValueMap());

            System.out.println("\nPlease select an option:");
            System.out.println("1. Update Information");
            System.out.println("2. Employees");
            System.out.println("3. Back");

            int choice = ConsoleUtils.readInt("\nEnter your choice (1-3): ", 1, 3);

            switch (choice) {
                case 1 -> updateClinicInfo();
                case 2 -> manageEmployees();
                case 3 -> {
                    return;
                }
            }
        }
    }

    private void updateClinicInfo() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Update Clinic Information");

        final Clinic clinic = ClinicService.get();
        System.out.println("\nCurrent Information:");
        ConsoleUtils.printModelInfo(clinic.toKeyValueMap());

        System.out.println("\nEnter new information (press Enter to keep current value):");

        System.out.print("Name [" + clinic.name + "]: ");
        String name = ConsoleUtils.readLine();
        if (!name.isEmpty())
            clinic.name = name;

        System.out.print("Address [" + clinic.address + "]: ");
        String address = ConsoleUtils.readLine();
        if (!address.isEmpty())
            clinic.address = address;

        System.out.print("Specialty [" + clinic.specialty + "]: ");
        String specialty = ConsoleUtils.readLine();
        if (!specialty.isEmpty())
            clinic.specialty = specialty;

        System.out.print("Phone [" + clinic.phone + "]: ");
        String phone = ConsoleUtils.readLine();
        if (!phone.isEmpty())
            clinic.phone = phone;

        ClinicService.save(clinic);
        System.out.println("\nClinic information updated successfully!");
        ConsoleUtils.waitForEnter();
    }

    private void manageEmployees() {
        while (true) {
            ConsoleUtils.clearScreen();
            ConsoleUtils.printTitle("Manage Employees");

            final Clinic clinic = ClinicService.get();
            List<Doctor> doctors = clinic.doctors;
            List<Assistant> assistants = clinic.assistants;

            System.out.println("\nDoctors:");
            ConsoleUtils.printModelList(doctors);

            System.out.println("\nAssistants:");
            ConsoleUtils.printModelList(assistants, doctors.size() + 1);

            System.out.println("\nPlease select an option:");
            System.out.println("1. Add");
            System.out.println("2. View Details");
            System.out.println("3. Back");

            int choice = ConsoleUtils.readInt("\nEnter your choice (1-3): ", 1, 3);

            switch (choice) {
                case 1 -> addEmployee();
                case 2 -> viewEmployeeDetails();
                case 3 -> {
                    return;
                }
            }
        }
    }

    private void addEmployee() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Add Employee");

        System.out.println("Select employee type:");
        System.out.println("1. Doctor");
        System.out.println("2. Assistant");

        int type = ConsoleUtils.readInt("\nEnter your choice (1-2): ", 1, 2);

        System.out.print("\nFirst Name: ");
        String firstName = ConsoleUtils.readLine();

        System.out.print("Last Name: ");
        String lastName = ConsoleUtils.readLine();

        System.out.print("Email: ");
        String email = ConsoleUtils.readLine();

        System.out.print("Username: ");
        String username = ConsoleUtils.readLine();

        System.out.print("Password: ");
        String password = ConsoleUtils.readLine();

        if (type == 1) {
            System.out.print("Specialty: ");
            String specialty = ConsoleUtils.readLine();

            Doctor doctor = new Doctor(null, firstName, lastName, email, username, password, specialty);
            UserService.saveDoctor(doctor);
            System.out.println("\nDoctor added successfully!");
        } else {
            Assistant assistant = new Assistant(null, firstName, lastName, email, username, password);
            UserService.saveAssistant(assistant);
            System.out.println("\nAssistant added successfully!");
        }

        ConsoleUtils.waitForEnter();
    }

    private void viewEmployeeDetails() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Employee Details");

        final Clinic clinic = ClinicService.get();
        List<Doctor> doctors = clinic.doctors;
        List<Assistant> assistants = clinic.assistants;

        System.out.println("\nDoctors:");
        ConsoleUtils.printModelList(doctors);

        System.out.println("\nAssistants:");
        ConsoleUtils.printModelList(assistants, doctors.size() + 1);

        int totalEmployees = doctors.size() + assistants.size();
        if (totalEmployees == 0) {
            System.out.println("\nNo employees available.");
            ConsoleUtils.waitForEnter();
            return;
        }

        int employeeIndex = ConsoleUtils.readInt("\nEnter employee number to view details (0 to go back): ", 0,
                totalEmployees) - 1;
        if (employeeIndex == -1)
            return;

        if (employeeIndex < doctors.size()) {
            Doctor doctor = doctors.get(employeeIndex);
            new EmployeeDetailsView(doctor).show();
        } else {
            Assistant assistant = assistants.get(employeeIndex - doctors.size());
            new EmployeeDetailsView(assistant).show();
        }
    }
}