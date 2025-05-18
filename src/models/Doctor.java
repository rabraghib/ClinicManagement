package models;

import utils.StringUtils;
import java.util.Date;
import java.util.Map;

public class Doctor extends User {

    public String specialty;

    public Doctor() {
        super();
    }

    public Doctor(Long id, String firstName, String lastName, String email, String username, String password,
            String specialty) {
        super(id, firstName, lastName, email, username, password);
        this.specialty = specialty;
    }

    public Prescription createPrescription(Patient patient, String medication, String description) {
        Prescription prescription = new Prescription();
        prescription.doctor = this;
        prescription.patient = patient;
        prescription.medication = medication;
        prescription.description = description;
        prescription.creationDate = new Date();
        return prescription;
    }

    @Override
    public String toFileString() {
        return StringUtils.listToFileString(super.toFileString(), specialty);
    }

    public static Doctor fromFileString(String str) {
        String[] parts = StringUtils.fileStringToList(str);
        if (parts.length >= 7) {
            Doctor doctor = new Doctor();
            doctor.id = Long.parseLong(parts[0]);
            doctor.firstName = parts[1];
            doctor.lastName = parts[2];
            doctor.email = parts[3];
            doctor.username = parts[4];
            doctor.password = parts[5];
            doctor.specialty = parts[6];
            return doctor;
        }
        return null;
    }

    @Override
    public Map<String, String> toKeyValueMap() {
        Map<String, String> map = super.toKeyValueMap();
        map.put("Specialty", specialty);
        return map;
    }
}