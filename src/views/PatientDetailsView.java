package views;

import models.Patient;
import models.Prescription;
import models.User;
import services.PatientService;
import services.PrescriptionService;
import utils.ConsoleUtils;
import utils.DateUtils;

import java.util.Date;
import java.util.List;

public class PatientDetailsView {
    private final Patient patient;

    public PatientDetailsView(Patient patient) {
        this.patient = patient;
    }

    public void show() {
        while (true) {
            final User currentUser = LoginView.getCurrentUser();
            ConsoleUtils.clearScreen();
            ConsoleUtils.printTitle("Patient Details");

            ConsoleUtils.printModelInfo(patient.toKeyValueMap());

            System.out.println("\nMedical Records:");
            List<Prescription> prescriptions = PrescriptionService.getByPatientId(patient.id);
            if (prescriptions.isEmpty()) {
                System.out.println("No medical records found.");
            } else {
                ConsoleUtils.printModelList(prescriptions);
            }

            System.out.println("\nPlease select an option:");
            System.out.println("1. Schedule New Appointment");
            if (currentUser instanceof models.Doctor) {
                System.out.println("2. Add Prescription");
            }
            System.out.println("3. Edit");
            System.out.println("4. Remove");
            System.out.println("5. Back");

            int choice = ConsoleUtils.readInt("\nEnter your choice (1-5): ", 1, 5);

            switch (choice) {
                case 1 -> scheduleAppointment();
                case 2 -> {
                    if (LoginView.getCurrentUser() instanceof models.Doctor) {
                        addPrescription();
                    }
                }
                case 3 -> editPatient();
                case 4 -> {
                    if (removePatient()) {
                        return;
                    }
                }
                case 5 -> {
                    return;
                }
            }
        }
    }

    private void scheduleAppointment() {
        new AppointmentView().scheduleAppointment(patient);
    }

    private void addPrescription() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Add Prescription");

        System.out.print("Medication: ");
        String medication = ConsoleUtils.readLine();

        System.out.print("Description: ");
        String description = ConsoleUtils.readLine();

        User currentUser = LoginView.getCurrentUser();
        if (currentUser instanceof models.Doctor doctor) {
            PrescriptionService.create(medication, description, doctor.id, patient.id);
            System.out.println("\nPrescription added successfully!");
            ConsoleUtils.waitForEnter();
        }
    }

    private void editPatient() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Edit Patient");

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

        System.out.print("Date of Birth [" + DateUtils.formatDate(patient.dateOfBirth) + "] (YYYY-MM-DD): ");
        String dateStr = ConsoleUtils.readLine();
        if (!dateStr.isEmpty()) {
            Date dateOfBirth = DateUtils.parseDate(dateStr);
            if (dateOfBirth != null)
                patient.dateOfBirth = dateOfBirth;
        }

        System.out.print("Gender [" + patient.gender + "]: ");
        String gender = ConsoleUtils.readLine();
        if (!gender.isEmpty())
            patient.gender = gender;

        PatientService.save(patient);

        System.out.println("\nPatient updated successfully!");
        ConsoleUtils.waitForEnter();
    }

    private boolean removePatient() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Remove Patient");

        boolean confirm = ConsoleUtils.readBool("Are you sure you want to remove this patient?");
        if (!confirm) {
            return false;
        }

        PatientService.remove(patient.id);
        System.out.println("\nPatient removed successfully!");
        ConsoleUtils.waitForEnter();
        return true;
    }
}
