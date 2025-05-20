package models;

import utils.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.LinkedHashMap;

import services.PatientService;
import services.UserService;

public class Clinic extends SerializableModel {

    public String name;
    public String address;
    public String specialty;
    public String phone;
    public List<Doctor> doctors;
    public List<Assistant> assistants;
    public List<Patient> patients;

    public Clinic() {
        this.doctors = new ArrayList<>();
        this.assistants = new ArrayList<>();
        this.patients = new ArrayList<>();
    }

    public Clinic(Long id, String name, String address, String specialty, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.specialty = specialty;
        this.phone = phone;
        this.doctors = new ArrayList<>();
        this.assistants = new ArrayList<>();
        this.patients = new ArrayList<>();
    }

    @Override
    public String toFileString() {
        return id + Constants.FILE_SEPARATOR +
                name + Constants.FILE_SEPARATOR +
                address + Constants.FILE_SEPARATOR +
                specialty + Constants.FILE_SEPARATOR +
                phone;
    }

    public static Clinic fromFileString(String str) {
        String[] parts = str.split(Pattern.quote(Constants.FILE_SEPARATOR));
        if (parts.length >= 5) {
            Clinic clinic = new Clinic();
            clinic.id = Long.parseLong(parts[0]);
            clinic.name = parts[1];
            clinic.address = parts[2];
            clinic.specialty = parts[3];
            clinic.phone = parts[4];
            clinic.doctors = UserService.getAllDoctors();
            clinic.assistants = UserService.getAllAssistants();
            clinic.patients = PatientService.getAll();
            return clinic;
        }
        return null;
    }

    @Override
    public String toViewListString() {
        return String.format("%s - %s", name, specialty);
    }

    @Override
    public Map<String, String> toKeyValueMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("Name", name);
        map.put("Address", address);
        map.put("Specialty", specialty);
        map.put("Phone", phone);
        map.put("Number of Doctors", String.valueOf(doctors != null ? doctors.size() : 0));
        map.put("Number of Assistants", String.valueOf(assistants != null ? assistants.size() : 0));
        map.put("Number of Patients", String.valueOf(patients != null ? patients.size() : 0));
        return map;
    }
}