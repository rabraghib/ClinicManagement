package services;

import models.Patient;
import models.MedicalRecord;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PatientService {

    private static final DataRepository<Patient> repo = new DataRepository<>("data/patients.txt",
            Patient::fromFileString);

    public static List<Patient> getAllPatients() {
        return repo.loadAll();
    }

    public static Patient savePatient(Patient patient) {
        return repo.save(patient);
    }

    public static Patient getPatientById(Long id) {
        return repo.loadById(id);
    }

    public static boolean removePatient(Long id) {
        return repo.remove(id);
    }

    public static List<Patient> findPatients(Predicate<Patient> predicate) {
        return getAllPatients().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static List<Patient> findPatientsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllPatients();
        }
        String searchTerm = name.toLowerCase().trim();
        return findPatients(p -> (p.firstName + " " + p.lastName).toLowerCase().contains(searchTerm) ||
                p.firstName.toLowerCase().contains(searchTerm) ||
                p.lastName.toLowerCase().contains(searchTerm));
    }

    public static Patient createPatient(String firstName, String lastName,
            Date dateOfBirth, String gender,
            String email, String phone) {
        Patient patient = new Patient(null, firstName, lastName, dateOfBirth, gender, email, phone);
        patient.medicalRecord = new MedicalRecord(patient);
        return savePatient(patient);
    }

    public static Patient updatePatient(Long id, String firstName, String lastName,
            Date dateOfBirth, String gender,
            String email, String phone) {
        Patient patient = getPatientById(id);
        if (patient == null) {
            return null;
        }

        patient.firstName = firstName;
        patient.lastName = lastName;
        patient.dateOfBirth = dateOfBirth;
        patient.gender = gender;
        patient.email = email;
        patient.phone = phone;

        return savePatient(patient);
    }
}