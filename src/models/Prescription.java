package models;

import utils.Constants;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.LinkedHashMap;

import services.PatientService;
import services.UserService;
import utils.DateUtils;

public class Prescription extends SerializableModel {
    public Date creationDate;
    public String medication;
    public String description;
    public Doctor doctor;
    public Patient patient;

    public Prescription() {
        this.creationDate = new Date();
    }

    public Prescription(Long id, String medication, String description, Doctor doctor, Patient patient) {
        this.id = id;
        this.medication = medication;
        this.description = description;
        this.doctor = doctor;
        this.patient = patient;
        this.creationDate = new Date();
    }

    @Override
    public String toFileString() {
        return id + Constants.FILE_SEPARATOR +
                DateUtils.formatDate(creationDate) + Constants.FILE_SEPARATOR +
                medication + Constants.FILE_SEPARATOR +
                description + Constants.FILE_SEPARATOR +
                (doctor != null ? doctor.id : "0") + Constants.FILE_SEPARATOR +
                (patient != null ? patient.id : "0");
    }

    public static Prescription fromFileString(String str) {
        String[] parts = str.split(Pattern.quote(Constants.FILE_SEPARATOR));
        if (parts.length >= 6) {
            Prescription prescription = new Prescription();
            prescription.id = Long.parseLong(parts[0]);
            prescription.creationDate = DateUtils.parseDate(parts[1]);
            prescription.medication = parts[2];
            prescription.description = parts[3];
            prescription.doctor = UserService.getDoctorById(Long.parseLong(parts[4]));
            prescription.patient = PatientService.getPatientById(Long.parseLong(parts[5]));
            return prescription;
        }
        return null;
    }

    public Map<String, String> toKeyValueMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("ID", String.valueOf(id));
        map.put("Creation Date", DateUtils.formatDate(creationDate));
        map.put("Medication", medication);
        map.put("Description", description);
        map.put("Doctor", doctor != null ? doctor.getFullName() : "N/A");
        map.put("Patient", patient != null ? patient.getFullName() : "N/A");
        return map;
    }
}