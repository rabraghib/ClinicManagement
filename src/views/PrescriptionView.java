package views;

import models.Prescription;
import models.Doctor;
import models.Patient;
import services.PrescriptionService;
import services.UserService;
import services.PatientService;
import utils.ConsoleUtils;
import java.util.List;
import java.util.Map;

public class PrescriptionView {
    public void show() {
        while (true) {
            ConsoleUtils.clearScreen();
            ConsoleUtils.printTitle("Prescription Management");

            System.out.println("\nPlease select an option:");
            System.out.println("1. View All Prescriptions");
            System.out.println("2. View Patient Prescriptions");
            System.out.println("3. Create New Prescription");
            System.out.println("4. Edit Prescription");
            System.out.println("5. Remove Prescription");
            System.out.println("6. Back to Main Menu");

            int choice = ConsoleUtils.readInt("\nEnter your choice (1-6): ", 1, 6);

            switch (choice) {
                case 1 -> viewAllPrescriptions();
                case 2 -> viewPatientPrescriptions();
                case 3 -> createPrescription();
                case 4 -> editPrescription();
                case 5 -> removePrescription();
                case 6 -> {
                    return;
                }
            }
        }
    }

    private void viewAllPrescriptions() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("All Prescriptions");

        List<Prescription> prescriptions = PrescriptionService.getAllPrescriptions();
        if (prescriptions.isEmpty()) {
            System.out.println("No prescriptions found.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> prescriptionsData = prescriptions.stream()
                .map(Prescription::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(prescriptionsData);
        ConsoleUtils.waitForEnter();
    }

    private void viewPatientPrescriptions() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Patient Prescriptions");

        List<Patient> patients = PatientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients available.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> patientsData = patients.stream()
                .map(Patient::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(patientsData);

        int patientIndex = ConsoleUtils.readInt("\nSelect patient (0 to cancel): ", 0, patients.size()) - 1;
        if (patientIndex == -1)
            return;

        Patient patient = patients.get(patientIndex);
        List<Prescription> prescriptions = PrescriptionService.getPatientPrescriptions(patient.id);

        if (prescriptions.isEmpty()) {
            System.out.println("\nNo prescriptions found for this patient.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> prescriptionsData = prescriptions.stream()
                .map(Prescription::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(prescriptionsData);
        ConsoleUtils.waitForEnter();
    }

    private void createPrescription() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Create New Prescription");

        // Select Patient
        List<Patient> patients = PatientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients available. Please add a patient first.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> patientsData = patients.stream()
                .map(Patient::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(patientsData);
        int patientIndex = ConsoleUtils.readInt("\nSelect patient (0 to cancel): ", 0, patients.size()) - 1;
        if (patientIndex == -1)
            return;
        Patient patient = patients.get(patientIndex);

        // Select Doctor
        List<Doctor> doctors = UserService.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("No doctors available. Please add a doctor first.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> doctorsData = doctors.stream()
                .map(Doctor::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(doctorsData);
        int doctorIndex = ConsoleUtils.readInt("\nSelect doctor (0 to cancel): ", 0, doctors.size()) - 1;
        if (doctorIndex == -1)
            return;
        Doctor doctor = doctors.get(doctorIndex);

        System.out.print("\nEnter medication: ");
        String medication = ConsoleUtils.readLine();

        System.out.print("Enter description: ");
        String description = ConsoleUtils.readLine();

        Prescription prescription = new Prescription(null, medication, description, doctor, patient);
        PrescriptionService.savePrescription(prescription);

        System.out.println("\nPrescription created successfully!");
        ConsoleUtils.waitForEnter();
    }

    private void editPrescription() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Edit Prescription");

        List<Prescription> prescriptions = PrescriptionService.getAllPrescriptions();
        if (prescriptions.isEmpty()) {
            System.out.println("No prescriptions available to edit.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> prescriptionsData = prescriptions.stream()
                .map(Prescription::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(prescriptionsData);

        int prescriptionIndex = ConsoleUtils.readInt("\nEnter prescription number to edit (0 to cancel): ", 0,
                prescriptions.size()) - 1;
        if (prescriptionIndex == -1)
            return;

        Prescription prescription = prescriptions.get(prescriptionIndex);

        System.out.println("\nEnter new information (press Enter to keep current value):");

        System.out.print("Medication [" + prescription.medication + "]: ");
        String medication = ConsoleUtils.readLine();
        if (!medication.isEmpty())
            prescription.medication = medication;

        System.out.print("Description [" + prescription.description + "]: ");
        String description = ConsoleUtils.readLine();
        if (!description.isEmpty())
            prescription.description = description;

        PrescriptionService.savePrescription(prescription);

        System.out.println("\nPrescription updated successfully!");
        ConsoleUtils.waitForEnter();
    }

    private void removePrescription() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Remove Prescription");

        List<Prescription> prescriptions = PrescriptionService.getAllPrescriptions();
        if (prescriptions.isEmpty()) {
            System.out.println("No prescriptions available to remove.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> prescriptionsData = prescriptions.stream()
                .map(Prescription::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(prescriptionsData);

        int prescriptionIndex = ConsoleUtils.readInt("\nEnter prescription number to remove (0 to cancel): ", 0,
                prescriptions.size()) - 1;
        if (prescriptionIndex == -1)
            return;

        Prescription prescription = prescriptions.get(prescriptionIndex);
        PrescriptionService.removePrescription(prescription.id);

        System.out.println("\nPrescription removed successfully!");
        ConsoleUtils.waitForEnter();
    }
}