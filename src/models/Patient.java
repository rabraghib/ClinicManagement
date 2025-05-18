package models;

import utils.Constants;
import utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.LinkedHashMap;

public class Patient extends SerializableModel {
    public String firstName;
    public String lastName;
    public String email;
    public String phone;
    public Date dateOfBirth;
    public String gender;
    public MedicalRecord medicalRecord;
    public List<Appointment> appointments;

    public Patient() {
        this.appointments = new ArrayList<>();
    }

    public Patient(Long id, String firstName, String lastName, Date dateOfBirth, String gender, String email,
            String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.appointments = new ArrayList<>();
        this.medicalRecord = new MedicalRecord(this);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toFileString() {
        return id + Constants.FILE_SEPARATOR +
                firstName + Constants.FILE_SEPARATOR +
                lastName + Constants.FILE_SEPARATOR +
                DateUtils.formatDate(dateOfBirth) + Constants.FILE_SEPARATOR +
                gender + Constants.FILE_SEPARATOR +
                email + Constants.FILE_SEPARATOR +
                phone;
    }

    public static Patient fromFileString(String str) {
        String[] parts = str.split(Pattern.quote(Constants.FILE_SEPARATOR));
        if (parts.length >= 7) {
            Patient patient = new Patient();
            patient.id = Long.parseLong(parts[0]);
            patient.firstName = parts[1];
            patient.lastName = parts[2];
            patient.dateOfBirth = DateUtils.parseDate(parts[3]);
            patient.gender = parts[4];
            patient.email = parts[5];
            patient.phone = parts[6];
            patient.medicalRecord = new MedicalRecord(patient);
            return patient;
        }
        return null;
    }

    public Map<String, String> toKeyValueMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("ID", String.valueOf(id));
        map.put("First Name", firstName);
        map.put("Last Name", lastName);
        map.put("Email", email);
        map.put("Phone", phone);
        map.put("Date of Birth", DateUtils.formatDate(dateOfBirth));
        map.put("Gender", gender);
        return map;
    }
}