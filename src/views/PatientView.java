package views;

import models.Patient;
import services.PatientService;
import utils.ConsoleUtils;
import utils.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class PatientView {
    public void show() {
        while (true) {
            ConsoleUtils.clearScreen();
            ConsoleUtils.printTitle("Patient Management");

            System.out.println("\nPlease select an option:");
            System.out.println("1. View All Patients");
            System.out.println("2. Search Patients");
            System.out.println("3. Add New Patient");
            System.out.println("4. Edit Patient");
            System.out.println("5. Remove Patient");
            System.out.println("6. Back to Main Menu");

            int choice = ConsoleUtils.readInt("\nEnter your choice (1-6): ", 1, 6);

            switch (choice) {
                case 1 -> viewAllPatients();
                case 2 -> searchPatients();
                case 3 -> addPatient();
                case 4 -> editPatient();
                case 5 -> removePatient();
                case 6 -> {
                    return;
                }
            }
        }
    }

    private void viewAllPatients() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("All Patients");

        List<Patient> patients = PatientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> patientsData = patients.stream()
                .map(Patient::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(patientsData);
        ConsoleUtils.waitForEnter();
    }

    private void searchPatients() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Search Patients");

        System.out.print("\nEnter search term: ");
        String searchTerm = ConsoleUtils.readLine();

        List<Patient> patients = PatientService.findPatients(
                p -> String.join(" ",
                        p.firstName,
                        p.lastName,
                        p.email,
                        p.phone).contains(searchTerm));
        if (patients.isEmpty()) {
            System.out.println("\nNo patients found matching the search criteria.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> patientsData = patients.stream()
                .map(Patient::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(patientsData);
        ConsoleUtils.waitForEnter();
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

        Patient patient = new Patient(null, firstName, lastName, dateOfBirth, gender, email, phone);
        PatientService.savePatient(patient);

        System.out.println("\nPatient added successfully!");
        ConsoleUtils.waitForEnter();
    }

    private void editPatient() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Edit Patient");

        List<Patient> patients = PatientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients available to edit.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> patientsData = patients.stream()
                .map(Patient::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(patientsData);

        int patientIndex = ConsoleUtils.readInt("\nEnter patient number to edit (0 to cancel): ", 0, patients.size())
                - 1;
        if (patientIndex == -1)
            return;

        Patient patient = patients.get(patientIndex);

        System.out.println("\nEnter new information (press Enter to keep current value):");

        System.out.print("First Name [" + patient.firstName + "]: ");
        String firstName = ConsoleUtils.readLine();
        if (!firstName.isEmpty())
            patient.firstName = firstName;

        System.out.print("Last Name [" + patient.lastName + "]: ");
        String lastName = ConsoleUtils.readLine();
        if (!lastName.isEmpty())
            patient.lastName = lastName;

        System.out.print("Email [" + patient.email + "]: ");
        String email = ConsoleUtils.readLine();
        if (!email.isEmpty())
            patient.email = email;

        System.out.print("Phone [" + patient.phone + "]: ");
        String phone = ConsoleUtils.readLine();
        if (!phone.isEmpty())
            patient.phone = phone;

        System.out.print("Date of Birth [" + patient.dateOfBirth + "]: ");
        Date dateOfBirth = DateUtils.parseDate(ConsoleUtils.readLine());
        if (dateOfBirth != null)
            patient.dateOfBirth = dateOfBirth;

        System.out.print("Gender [" + patient.gender + "]: ");
        String gender = ConsoleUtils.readLine();
        if (!gender.isEmpty())
            patient.gender = gender;

        PatientService.savePatient(patient);

        System.out.println("\nPatient updated successfully!");
        ConsoleUtils.waitForEnter();
    }

    private void removePatient() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Remove Patient");

        List<Patient> patients = PatientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients available to remove.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> patientsData = patients.stream()
                .map(Patient::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(patientsData);

        int patientIndex = ConsoleUtils.readInt("\nEnter patient number to remove (0 to cancel): ", 0, patients.size())
                - 1;
        if (patientIndex == -1)
            return;

        Patient patient = patients.get(patientIndex);
        PatientService.removePatient(patient.id);

        System.out.println("\nPatient removed successfully!");
        ConsoleUtils.waitForEnter();
    }
}