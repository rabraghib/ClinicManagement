package services;

import models.MedicalRecord;
import models.Patient;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MedicalRecordService {

    private static final DataRepository<MedicalRecord> repo = new DataRepository<>("data/medical-records.txt",
            MedicalRecord::fromFileString);

    public static List<MedicalRecord> getAll() {
        return repo.loadAll();
    }

    public static MedicalRecord save(MedicalRecord record) {
        return repo.save(record);
    }

    public static MedicalRecord getById(Long id) {
        return repo.loadById(id);
    }

    public static MedicalRecord getByPatientId(Long patientId) {
        List<MedicalRecord> records = find(r -> r.getPatient() != null &&
                r.getPatient().id.equals(patientId));
        return records.isEmpty() ? null : records.get(0);
    }

    public static boolean remove(Long id) {
        return repo.remove(id);
    }

    public static List<MedicalRecord> find(Predicate<MedicalRecord> predicate) {
        List<MedicalRecord> records = getAll();
        return records.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static MedicalRecord updateHistory(Long id, String newEntry) {
        MedicalRecord record = getById(id);
        if (record == null) {
            return null;
        }

        record.updateHistory(newEntry);
        return save(record);
    }

    public static MedicalRecord create(Patient patient) {
        MedicalRecord record = new MedicalRecord(patient);
        return save(record);
    }
}