package models;

import utils.DateUtils;
import utils.StringUtils;

import java.util.Date;
import java.util.Map;

import services.MedicalRecordService;

import java.util.LinkedHashMap;

public class Patient extends SerializableModel {
    public String firstName;
    public String lastName;
    public String email;
    public String phone;
    public Date dateOfBirth;
    public String gender;
    public MedicalRecord medicalRecord;
    // public List<Appointment> appointments;

    public Patient() {
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
        this.medicalRecord = new MedicalRecord(this);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toFileString() {
        return StringUtils.toFileString(
                String.valueOf(id),
                firstName,
                lastName,
                DateUtils.formatDate(dateOfBirth),
                gender,
                email,
                phone);
    }

    public static Patient fromFileString(String str) {
        String[] parts = StringUtils.fileStringToList(str);
        if (parts.length >= 7) {
            Patient patient = new Patient();
            patient.id = Long.parseLong(parts[0]);
            patient.firstName = parts[1];
            patient.lastName = parts[2];
            patient.dateOfBirth = DateUtils.parseDate(parts[3]);
            patient.gender = parts[4];
            patient.email = parts[5];
            patient.phone = parts[6];
            patient.medicalRecord = MedicalRecordService.getByPatientId(patient.id);
            return patient;
        }
        return null;
    }

    @Override
    public String toViewListString() {
        return String.format("%s (%s)", getFullName(), email);
    }

    @Override
    public Map<String, String> toKeyValueMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("ID", String.valueOf(id));
        map.put("Name", getFullName());
        map.put("Email", email);
        map.put("Phone", phone);
        map.put("Date of Birth", DateUtils.formatDate(dateOfBirth));
        map.put("Gender", gender);
        return map;
    }
}