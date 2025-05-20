package views;

import models.Patient;
import services.PatientService;
import utils.ConsoleUtils;
import utils.DateUtils;

import java.util.Date;
import java.util.List;

public class PatientView {
    public void show() {
        while (true) {
            ConsoleUtils.clearScreen();
            ConsoleUtils.printTitle("Patients");

            System.out.println("\nPlease select an option:");
            System.out.println("1. View All");
            System.out.println("2. Search");
            System.out.println("3. Register Patient");
            System.out.println("4. Back");

            int choice = ConsoleUtils.readInt("\nEnter your choice (1-4): ", 1, 4);

            switch (choice) {
                case 1 -> viewAllPatients();
                case 2 -> searchPatients();
                case 3 -> addPatient();
                case 4 -> {
                    return;
                }
            }
        }
    }

    private void viewAllPatients() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("All Patients");

        List<Patient> patients = PatientService.getAll();
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
            ConsoleUtils.waitForEnter();
            return;
        }

        ConsoleUtils.printModelList(patients);

        int patientIndex = ConsoleUtils.readInt("\nEnter patient number to view details (0 to go back): ", 0,
                patients.size()) - 1;
        if (patientIndex == -1)
            return;

        Patient patient = patients.get(patientIndex);
        new PatientDetailsView(patient).show();
    }

    private void searchPatients() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Search Patients");

        System.out.print("\nEnter search term: ");
        String searchTerm = ConsoleUtils.readLine();

        List<Patient> patients = PatientService.findByName(searchTerm);
        if (patients.isEmpty()) {
            System.out.println("\nNo patients found matching the search criteria.");
            ConsoleUtils.waitForEnter();
            return;
        }
        ConsoleUtils.printModelList(patients);

        int patientIndex = ConsoleUtils.readInt("\nEnter patient number to view details (0 to go back): ", 0,
                patients.size()) - 1;
        if (patientIndex == -1)
            return;

        Patient patient = patients.get(patientIndex);
        new PatientDetailsView(patient).show();
    }

    private void addPatient() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Add New Patient");

        System.out.print("First Name: ");
        String firstName = ConsoleUtils.readLine();

        System.out.print("Last Name: ");
        String lastName = ConsoleUtils.readLine();

        System.out.print("Email: ");
        String email = ConsoleUtils.readLine();

        System.out.print("Phone: ");
        String phone = ConsoleUtils.readLine();

        System.out.print("Date of Birth (YYYY-MM-DD): ");
        Date dateOfBirth = DateUtils.parseDate(ConsoleUtils.readLine());

        System.out.print("Gender (M/F): ");
        String gender = ConsoleUtils.readLine();

        PatientService.create(firstName, lastName, dateOfBirth, gender, email, phone);
        System.out.println("\nPatient added successfully!");
        ConsoleUtils.waitForEnter();
    }

}