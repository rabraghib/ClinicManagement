package services;

import models.Prescription;
import models.Doctor;
import models.Patient;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PrescriptionService {

    private static final DataRepository<Prescription> prescriptionRepo = new DataRepository<>("data/prescriptions.txt",
            Prescription::fromFileString);

    public static List<Prescription> getAll() {
        return prescriptionRepo.loadAll();
    }

    public static Prescription save(Prescription prescription) {
        if (prescription.id == null) {
            prescription.id = DataRepository.generateId();
        }
        return prescriptionRepo.save(prescription);
    }

    public static Prescription getById(Long id) {
        return prescriptionRepo.loadById(id);
    }

    public static boolean remove(Long id) {
        return prescriptionRepo.remove(id);
    }

    public static List<Prescription> find(Predicate<Prescription> predicate) {
        List<Prescription> prescriptions = getAll();
        return prescriptions.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static Prescription create(String medication, String description,
            Long doctorId, Long patientId) {
        Doctor doctor = UserService.getDoctorById(doctorId);
        Patient patient = PatientService.getById(patientId);

        if (doctor == null || patient == null) {
            return null;
        }

        Prescription prescription = new Prescription(null, medication, description, doctor, patient);

        if (patient.medicalRecord != null) {
            patient.medicalRecord.prescriptions.add(prescription);
            MedicalRecordService.save(patient.medicalRecord);
        }

        return save(prescription);
    }

    public static List<Prescription> getByPatientId(Long patientId) {
        return find(p -> p.patient != null &&
                p.patient.id.equals(patientId));
    }

    public static List<Prescription> getByDoctorId(Long doctorId) {
        return find(p -> p.doctor != null &&
                p.doctor.id.equals(doctorId));
    }

}