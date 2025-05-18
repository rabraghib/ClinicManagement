package services;

import models.Clinic;
import java.util.List;

public class ClinicService {
    private static final Long clinicId = 1L;
    private static final DataRepository<Clinic> repo = new DataRepository<>("data/clinic.txt",
            Clinic::fromFileString);

    public static void save(Clinic clinic) {
        clinic.id = clinicId;
        repo.saveAll(List.of(clinic));
    }

    public static Clinic getClinic() {
        Clinic clinic = repo.loadById(clinicId);
        if (clinic == null) {
            clinic = new Clinic(clinicId, "ENSAM Clinic", "Sidi Othman, Casablanca", "General", "0600-123456");
            save(clinic);
        }
        clinic.doctors = UserService.getAllDoctors();
        clinic.assistants = UserService.getAllAssistants();
        clinic.patients = PatientService.getAllPatients();
        return clinic;
    }
}