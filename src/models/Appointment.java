package models;

import services.PatientService;
import services.UserService;
import utils.Constants;
import utils.DateUtils;

import java.util.Date;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.LinkedHashMap;

public class Appointment extends SerializableModel {
    public Date date;
    public int hour;
    public Patient patient;
    public Doctor doctor;
    public String notes = "";
    public boolean completed = false;

    public Appointment() {
    }

    public Appointment(Long id, Date date, int hour, Patient patient, Doctor doctor) {
        this.id = id;
        this.date = date;
        this.hour = hour;
        this.patient = patient;
        this.doctor = doctor;
        this.completed = false;
        this.notes = "";
    }

    @Override
    public String toFileString() {
        return id + Constants.FILE_SEPARATOR +
                DateUtils.formatDate(date) + Constants.FILE_SEPARATOR +
                hour + Constants.FILE_SEPARATOR +
                (patient != null ? patient.id : "0") + Constants.FILE_SEPARATOR +
                (doctor != null ? doctor.id : "0") + Constants.FILE_SEPARATOR +
                notes + Constants.FILE_SEPARATOR +
                completed;
    }

    public static Appointment fromFileString(String str) {
        String[] parts = str.split(Pattern.quote(Constants.FILE_SEPARATOR));
        if (parts.length >= 7) {
            Appointment appointment = new Appointment();
            appointment.id = Long.parseLong(parts[0]);
            appointment.date = DateUtils.parseDate(parts[1]);
            appointment.hour = Integer.parseInt(parts[2]);
            appointment.patient = PatientService.getPatientById(Long.parseLong(parts[3]));
            appointment.doctor = UserService.getDoctorById(Long.parseLong(parts[4]));
            appointment.notes = parts[5];
            appointment.completed = Boolean.parseBoolean(parts[6]);

            return appointment;
        }
        return null;
    }

    public Map<String, String> toKeyValueMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("ID", String.valueOf(id));
        map.put("Date", DateUtils.formatDate(date));
        map.put("Hour", String.valueOf(hour));
        map.put("Patient", patient != null ? patient.getFullName() : "N/A");
        map.put("Doctor", doctor != null ? doctor.getFullName() : "N/A");
        map.put("Notes", notes);
        map.put("Completed", completed ? "Yes" : "No");
        return map;
    }
}