package models;

import services.PatientService;
import services.UserService;
import utils.DateUtils;
import utils.StringUtils;

import java.util.Date;
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
        return StringUtils.listToFileString(
                String.valueOf(id),
                DateUtils.formatDate(date),
                String.valueOf(hour),
                patient != null ? String.valueOf(patient.id) : "0",
                doctor != null ? String.valueOf(doctor.id) : "0",
                notes,
                String.valueOf(completed));
    }

    public static Appointment fromFileString(String str) {
        String[] parts = StringUtils.fileStringToList(str);
        if (parts.length >= 7) {
            Appointment appointment = new Appointment();
            appointment.id = Long.parseLong(parts[0]);
            appointment.date = DateUtils.parseDate(parts[1]);
            appointment.hour = Integer.parseInt(parts[2]);
            appointment.patient = PatientService.getById(Long.parseLong(parts[3]));
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