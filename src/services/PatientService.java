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

    public static List<Patient> getAll() {
        return repo.loadAll();
    }

    public static Patient save(Patient patient) {
        return repo.save(patient);
    }

    public static Patient getById(Long id) {
        return repo.loadById(id);
    }

    public static boolean remove(Long id) {
        return repo.remove(id);
    }

    public static List<Patient> find(Predicate<Patient> predicate) {
        return getAll().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static List<Patient> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAll();
        }
        String searchTerm = name.toLowerCase().trim();
        return find(p -> (p.firstName + " " + p.lastName).toLowerCase().contains(searchTerm) ||
                p.firstName.toLowerCase().contains(searchTerm) ||
                p.lastName.toLowerCase().contains(searchTerm));
    }

    public static Patient create(String firstName, String lastName,
            Date dateOfBirth, String gender,
            String email, String phone) {
        Patient patient = new Patient(null, firstName, lastName, dateOfBirth, gender, email, phone);
        patient.medicalRecord = new MedicalRecord(patient);
        return save(patient);
    }

    public static Patient update(Long id, String firstName, String lastName,
            Date dateOfBirth, String gender,
            String email, String phone) {
        Patient patient = getById(id);
        if (patient == null) {
            return null;
        }

        patient.firstName = firstName;
        patient.lastName = lastName;
        patient.dateOfBirth = dateOfBirth;
        patient.gender = gender;
        patient.email = email;
        patient.phone = phone;

        return save(patient);
    }
}