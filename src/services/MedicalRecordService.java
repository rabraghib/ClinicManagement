package services;

import models.MedicalRecord;
import models.Patient;
import models.Prescription;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MedicalRecordService {

    private static final DataRepository<MedicalRecord> repo = new DataRepository<>("data/medical-records.txt",
            MedicalRecord::fromFileString);

    public static List<MedicalRecord> getAllMedicalRecords() {
        return repo.loadAll();
    }

    public static MedicalRecord saveMedicalRecord(MedicalRecord record) {
        return repo.save(record);
    }

    public static MedicalRecord getMedicalRecordById(Long id) {
        return repo.loadById(id);
    }

    public static MedicalRecord getMedicalRecordByPatientId(Long patientId) {
        List<MedicalRecord> records = findMedicalRecords(r -> r.getPatient() != null &&
                r.getPatient().id.equals(patientId));
        return records.isEmpty() ? null : records.get(0);
    }

    public static boolean removeMedicalRecord(Long id) {
        return repo.remove(id);
    }

    public static List<MedicalRecord> findMedicalRecords(Predicate<MedicalRecord> predicate) {
        List<MedicalRecord> records = getAllMedicalRecords();
        return records.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static MedicalRecord updateMedicalRecordHistory(Long id, String newEntry) {
        MedicalRecord record = getMedicalRecordById(id);
        if (record == null) {
            return null;
        }

        record.updateHistory(newEntry);
        return saveMedicalRecord(record);
    }

    public static MedicalRecord addPrescriptionToMedicalRecord(Long recordId, Prescription prescription) {
        MedicalRecord record = getMedicalRecordById(recordId);
        if (record == null) {
            return null;
        }

        record.prescriptions.add(prescription);
        return saveMedicalRecord(record);
    }

    public static MedicalRecord createMedicalRecord(Patient patient) {
        MedicalRecord record = new MedicalRecord(patient);
        return saveMedicalRecord(record);
    }
}