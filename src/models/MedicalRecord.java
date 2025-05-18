package models;

import utils.Constants;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.LinkedHashMap;

import services.PatientService;

public class MedicalRecord extends SerializableModel {

    private Patient patient;
    public String history;
    public List<Prescription> prescriptions;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        this.id = patient.id;
    }

    public MedicalRecord() {
        this.history = "";
        this.prescriptions = new ArrayList<>();
    }

    public MedicalRecord(Patient patient) {
        this.setPatient(patient);
        this.history = "";
        this.prescriptions = new ArrayList<>();
    }

    public void updateHistory(String newEntry) {
        if (history == null) {
            history = "";
        }
        history += "\n" + new Date() + ": " + newEntry;
    }

    @Override
    public String toFileString() {
        return id + Constants.FILE_SEPARATOR +
                (patient != null ? patient.id : "0") + Constants.FILE_SEPARATOR +
                history;
    }

    public static MedicalRecord fromFileString(String str) {
        String[] parts = str.split(Pattern.quote(Constants.FILE_SEPARATOR));
        if (parts.length >= 3) {
            MedicalRecord record = new MedicalRecord();
            record.id = Long.parseLong(parts[0]);
            record.patient = PatientService.getPatientById(Long.parseLong(parts[1]));
            record.history = parts[2];
            return record;
        }
        return null;
    }

    public Map<String, String> toKeyValueMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("ID", String.valueOf(id));
        map.put("Patient", patient != null ? patient.getFullName() : "N/A");
        map.put("History", history != null ? history : "No history");
        map.put("Number of Prescriptions", String.valueOf(prescriptions != null ? prescriptions.size() : 0));
        return map;
    }
}