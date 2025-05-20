package models;

import services.PatientService;
import utils.StringUtils;
import java.util.LinkedHashMap;
import java.util.Map;

public class MedicalRecord extends SerializableModel {

    private Patient patient;
    public String history;
    // public List<Prescription> prescriptions;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        this.id = patient.id;
    }

    public MedicalRecord() {
        this.history = "";
    }

    public MedicalRecord(Patient patient) {
        this.history = "";
        this.setPatient(patient);
        this.history = "";
    }

    public void updateHistory(String newEntry) {
        if (history == null || history.isEmpty()) {
            history = newEntry;
        } else {
            history += "\n" + newEntry;
        }
    }

    @Override
    public String toFileString() {
        return StringUtils.toFileString(
                String.valueOf(id),
                patient != null ? String.valueOf(patient.id) : "0",
                history);
    }

    public static MedicalRecord fromFileString(String str) {
        String[] parts = StringUtils.fileStringToList(str);
        if (parts.length >= 3) {
            MedicalRecord record = new MedicalRecord();
            record.id = Long.parseLong(parts[0]);
            record.patient = PatientService.getById(Long.parseLong(parts[1]));
            record.history = parts[2];
            return record;
        }
        return null;
    }

    @Override
    public String toViewListString() {
        return String.format("Record for %s", patient != null ? patient.getFullName() : "N/A");
    }

    @Override
    public Map<String, String> toKeyValueMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("ID", String.valueOf(id));
        map.put("Patient", patient != null ? patient.getFullName() : "N/A");
        map.put("History", history);
        return map;
    }
}