package views;

import models.Clinic;
import models.Doctor;
import models.Assistant;
import services.ClinicService;
import services.UserService;
import utils.ConsoleUtils;

import java.util.List;
import java.util.Map;

public class ClinicView {

    public void show() {
        while (true) {
            ConsoleUtils.clearScreen();
            ConsoleUtils.printTitle("Clinic Management");

            final Clinic clinic = ClinicService.get();
            ConsoleUtils.printModelInfo(clinic.toKeyValueMap());

            System.out.println("\nPlease select an option:");
            System.out.println("1. Update Clinic Information");
            System.out.println("2. Manage Doctors");
            System.out.println("3. Manage Assistants");
            System.out.println("4. Back to Main Menu");

            int choice = ConsoleUtils.readInt("\nEnter your choice (1-4): ", 1, 4);

            switch (choice) {
                case 1 -> updateClinicInfo();
                case 2 -> manageDoctors();
                case 3 -> manageAssistants();
                case 4 -> {
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

    private void manageDoctors() {
        while (true) {
            ConsoleUtils.clearScreen();
            ConsoleUtils.printTitle("Manage Doctors");

            final Clinic clinic = ClinicService.get();
            List<Doctor> doctors = clinic.doctors;
            List<Map<String, String>> doctorsData = doctors.stream()
                    .map(Doctor::toKeyValueMap)
                    .toList();

            ConsoleUtils.printModelList(doctorsData);

            System.out.println("\nPlease select an option:");
            System.out.println("1. Add New Doctor");
            System.out.println("2. Edit Doctor");
            System.out.println("3. Remove Doctor");
            System.out.println("4. Back to Clinic Management");

            int choice = ConsoleUtils.readInt("\nEnter your choice (1-4): ", 1, 4);

            switch (choice) {
                case 1 -> addDoctor();
                case 2 -> editDoctor();
                case 3 -> removeDoctor();
                case 4 -> {
                    return;
                }
            }
        }
    }

    private void manageAssistants() {
        while (true) {
            ConsoleUtils.clearScreen();
            ConsoleUtils.printTitle("Manage Assistants");

            final Clinic clinic = ClinicService.get();
            List<Assistant> assistants = clinic.assistants;
            List<Map<String, String>> assistantsData = assistants.stream()
                    .map(Assistant::toKeyValueMap)
                    .toList();

            ConsoleUtils.printModelList(assistantsData);

            System.out.println("\nPlease select an option:");
            System.out.println("1. Add New Assistant");
            System.out.println("2. Edit Assistant");
            System.out.println("3. Remove Assistant");
            System.out.println("4. Back to Clinic Management");

            int choice = ConsoleUtils.readInt("\nEnter your choice (1-4): ", 1, 4);

            switch (choice) {
                case 1 -> addAssistant();
                case 2 -> editAssistant();
                case 3 -> removeAssistant();
                case 4 -> {
                    return;
                }
            }
        }
    }

    private void addDoctor() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Add New Doctor");

        System.out.print("First Name: ");
        String firstName = ConsoleUtils.readLine();

        System.out.print("Last Name: ");
        String lastName = ConsoleUtils.readLine();

        System.out.print("Email: ");
        String email = ConsoleUtils.readLine();

        System.out.print("Username: ");
        String username = ConsoleUtils.readLine();

        System.out.print("Password: ");
        String password = ConsoleUtils.readLine();

        System.out.print("Specialty: ");
        String specialty = ConsoleUtils.readLine();

        Doctor doctor = new Doctor(null, firstName, lastName, email, username, password, specialty);
        UserService.saveDoctor(doctor);

        System.out.println("\nDoctor added successfully!");
        ConsoleUtils.waitForEnter();
    }

    private void editDoctor() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Edit Doctor");

        final Clinic clinic = ClinicService.get();
        List<Doctor> doctors = clinic.doctors;
        if (doctors.isEmpty()) {
            System.out.println("No doctors available to edit.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> doctorsData = doctors.stream()
                .map(Doctor::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(doctorsData);

        int doctorIndex = ConsoleUtils.readInt("\nEnter doctor number to edit (0 to cancel): ", 0, doctors.size()) - 1;
        if (doctorIndex == -1)
            return;

        Doctor doctor = doctors.get(doctorIndex);

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
        ConsoleUtils.waitForEnter();
    }

    private void removeDoctor() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Remove Doctor");

        final Clinic clinic = ClinicService.get();
        List<Doctor> doctors = clinic.doctors;
        if (doctors.isEmpty()) {
            System.out.println("No doctors available to remove.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> doctorsData = doctors.stream()
                .map(Doctor::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(doctorsData);

        int doctorIndex = ConsoleUtils.readInt("\nEnter doctor number to remove (0 to cancel): ", 0, doctors.size())
                - 1;
        if (doctorIndex == -1)
            return;

        Doctor doctor = doctors.get(doctorIndex);
        UserService.removeDoctor(doctor.id);

        System.out.println("\nDoctor removed successfully!");
        ConsoleUtils.waitForEnter();
    }

    private void addAssistant() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Add New Assistant");

        System.out.print("First Name: ");
        String firstName = ConsoleUtils.readLine();

        System.out.print("Last Name: ");
        String lastName = ConsoleUtils.readLine();

        System.out.print("Email: ");
        String email = ConsoleUtils.readLine();

        System.out.print("Username: ");
        String username = ConsoleUtils.readLine();

        System.out.print("Password: ");
        String password = ConsoleUtils.readLine();

        Assistant assistant = new Assistant(null, firstName, lastName, email, username, password);
        UserService.saveAssistant(assistant);

        System.out.println("\nAssistant added successfully!");
        ConsoleUtils.waitForEnter();
    }

    private void editAssistant() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Edit Assistant");

        final Clinic clinic = ClinicService.get();
        List<Assistant> assistants = clinic.assistants;
        if (assistants.isEmpty()) {
            System.out.println("No assistants available to edit.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> assistantsData = assistants.stream()
                .map(Assistant::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(assistantsData);

        int assistantIndex = ConsoleUtils.readInt("\nEnter assistant number to edit (0 to cancel): ", 0,
                assistants.size()) - 1;
        if (assistantIndex == -1)
            return;

        Assistant assistant = assistants.get(assistantIndex);

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
        ConsoleUtils.waitForEnter();
    }

    private void removeAssistant() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Remove Assistant");

        final Clinic clinic = ClinicService.get();
        List<Assistant> assistants = clinic.assistants;
        if (assistants.isEmpty()) {
            System.out.println("No assistants available to remove.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> assistantsData = assistants.stream()
                .map(Assistant::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(assistantsData);

        int assistantIndex = ConsoleUtils.readInt("\nEnter assistant number to remove (0 to cancel): ", 0,
                assistants.size()) - 1;
        if (assistantIndex == -1)
            return;

        Assistant assistant = assistants.get(assistantIndex);
        UserService.removeAssistant(assistant.id);

        System.out.println("\nAssistant removed successfully!");
        ConsoleUtils.waitForEnter();
    }
}