package models;

import services.PatientService;
import services.UserService;
import utils.DateUtils;
import utils.StringUtils;
import java.util.Map;
import java.util.Date;
import java.util.LinkedHashMap;

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
    }

    @Override
    public String toFileString() {
        return StringUtils.toFileString(
                String.valueOf(id),
                DateUtils.formatDate(creationDate),
                medication,
                description,
                doctor != null ? String.valueOf(doctor.id) : "0",
                patient != null ? String.valueOf(patient.id) : "0");
    }

    public static Prescription fromFileString(String str) {
        String[] parts = StringUtils.fileStringToList(str);
        if (parts.length >= 6) {
            Prescription prescription = new Prescription();
            prescription.id = Long.parseLong(parts[0]);
            prescription.creationDate = DateUtils.parseDate(parts[1]);
            prescription.medication = parts[2];
            prescription.description = parts[3];
            prescription.doctor = UserService.getDoctorById(Long.parseLong(parts[4]));
            prescription.patient = PatientService.getById(Long.parseLong(parts[5]));
            return prescription;
        }
        return null;
    }

    @Override
    public String toViewListString() {
        return String.format("%s - %s for %s",
                DateUtils.formatDate(creationDate),
                medication,
                patient != null ? patient.getFullName() : "N/A");
    }

    @Override
    public Map<String, String> toKeyValueMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("ID", String.valueOf(id));
        map.put("Date", DateUtils.formatDate(creationDate));
        map.put("Medication", medication);
        map.put("Description", description);
        map.put("Doctor", doctor != null ? doctor.getFullName() : "N/A");
        map.put("Patient", patient != null ? patient.getFullName() : "N/A");
        return map;
    }
}