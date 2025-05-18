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

    public static List<Prescription> getAllPrescriptions() {
        return prescriptionRepo.loadAll();
    }

    public static Prescription savePrescription(Prescription prescription) {
        if (prescription.id == null) {
            prescription.id = DataRepository.generateId();
        }
        return prescriptionRepo.save(prescription);
    }

    public static Prescription getPrescriptionById(Long id) {
        return prescriptionRepo.loadById(id);
    }

    public static boolean removePrescription(Long id) {
        return prescriptionRepo.remove(id);
    }

    public static List<Prescription> findPrescriptions(Predicate<Prescription> predicate) {
        List<Prescription> prescriptions = getAllPrescriptions();
        return prescriptions.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static Prescription createPrescription(String medication, String description,
            Long doctorId, Long patientId) {
        Doctor doctor = UserService.getDoctorById(doctorId);
        Patient patient = PatientService.getPatientById(patientId);

        if (doctor == null || patient == null) {
            return null;
        }

        Prescription prescription = new Prescription(null, medication, description, doctor, patient);

        if (patient.medicalRecord != null) {
            patient.medicalRecord.prescriptions.add(prescription);
            MedicalRecordService.saveMedicalRecord(patient.medicalRecord);
        }

        return savePrescription(prescription);
    }

    public static List<Prescription> getPatientPrescriptions(Long patientId) {
        return findPrescriptions(p -> p.patient != null &&
                p.patient.id.equals(patientId));
    }

    public static List<Prescription> getDoctorPrescriptions(Long doctorId) {
        return findPrescriptions(p -> p.doctor != null &&
                p.doctor.id.equals(doctorId));
    }

}