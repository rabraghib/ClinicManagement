package services;

import models.Appointment;
import models.Doctor;
import models.Patient;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AppointmentService {

    private static final DataRepository<Appointment> repo = new DataRepository<>("data/appointments.txt",
            Appointment::fromFileString);

    public static List<Appointment> getAll() {
        return repo.loadAll();
    }

    public static Appointment save(Appointment appointment) {
        return repo.save(appointment);
    }

    public static Appointment getById(Long id) {
        return repo.loadById(id);
    }

    public static boolean remove(Long id) {
        return repo.remove(id);
    }

    public static List<Appointment> find(Predicate<Appointment> predicate) {
        List<Appointment> appointments = getAll();
        return appointments.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static Appointment create(Date date, int hour, Long patientId, Long doctorId) {
        Patient patient = PatientService.getById(patientId);
        Doctor doctor = UserService.getDoctorById(doctorId);
        if (patient == null || doctor == null) {
            return null;
        }
        if (isTimeSlotBooked(date, hour, doctorId)) {
            return null;
        }
        Appointment appointment = new Appointment(null, date, hour, patient, doctor);
        return save(appointment);
    }

    public static Appointment update(Long id, Date date, int hour, boolean completed, String notes) {
        Appointment appointment = getById(id);
        if (appointment == null) {
            return null;
        }
        if ((date != appointment.date || hour != appointment.hour) &&
                isTimeSlotBooked(date, hour, appointment.doctor.id)) {
            return null;
        }
        appointment.date = date;
        appointment.hour = hour;
        appointment.completed = completed;
        appointment.notes = notes;
        return save(appointment);
    }

    public static boolean isTimeSlotBooked(Date date, int hour, Long doctorId) {
        List<Appointment> doctorAppointments = find(a -> a.doctor != null && a.doctor.id.equals(doctorId));
        for (Appointment existing : doctorAppointments) {
            if (isSameDay(existing.date, date) && existing.hour == hour) {
                return true;
            }
        }
        return false;
    }

    public static List<Appointment> getByDoctorIdAndDate(Long doctorId, Date date) {
        return find(a -> a.doctor != null &&
                a.doctor.id.equals(doctorId) &&
                isSameDay(a.date, date));
    }

    public static List<Appointment> getByPatientId(Long patientId) {
        return find(a -> a.patient != null &&
                a.patient.id.equals(patientId));
    }

    public static List<Appointment> getToday() {
        Date today = new Date();
        return find(a -> isSameDay(a.date, today));
    }

    public static List<Appointment> getPending() {
        return find(a -> !a.completed);
    }

    private static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
}