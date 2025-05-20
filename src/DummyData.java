
import models.*;
import services.*;
import utils.DateUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class DummyData {
    private static final String[] FIRST_NAMES = {
            "Mohammed", "Fatima", "Ahmed", "Amina", "Youssef", "Laila", "Karim", "Sara", "Omar", "Nadia",
            "Rachid", "Samira", "Hassan", "Meriem", "Mehdi", "Khadija", "Adil", "Zineb", "Reda", "Houda",
            "Amine", "Imane", "Younes", "Soukaina", "Bilal", "Salma", "Anas", "Ghita", "Hamza", "Naima"
    };

    private static final String[] LAST_NAMES = {
            "Alaoui", "Benali", "Cherkaoui", "Daoudi", "El Fathi", "Gharbi", "Hassani", "Idrissi", "Jabri", "Khalil",
            "Lahlou", "Mansouri", "Naciri", "Ouali", "Rahmani", "Slimani", "Tazi", "Ziani", "Bennani", "Chraibi",
            "El Malki", "Fassi", "Guerouani", "Hajji", "Ibnou", "Jazouli", "Kettani", "Lakhdar", "Mekouar", "Naji"
    };

    private static final String[] SPECIALTIES = {
            "Cardiology", "Pediatrics", "Neurology", "Dermatology", "Orthopedics",
            "Gynecology", "Ophthalmology", "Psychiatry", "Urology", "Endocrinology"
    };

    private static final String[] MEDICATIONS = {
            "Amoxicillin", "Ibuprofen", "Lisinopril", "Metformin", "Atorvastatin",
            "Omeprazole", "Albuterol", "Metoprolol", "Gabapentin", "Sertraline",
            "Levothyroxine", "Amlodipine", "Hydrochlorothiazide", "Simvastatin", "Losartan"
    };

    private static final String[] GENDERS = { "M", "F" };
    private static final int[] BIRTH_YEARS = { 1980, 1985, 1990, 1995, 2000 };
    private static final int[] APPOINTMENT_HOURS = { 9, 10, 11, 12, 13, 14, 15, 16 };

    private static List<Doctor> doctors = new ArrayList<>();
    private static List<Assistant> assistants = new ArrayList<>();
    private static List<Patient> patients = new ArrayList<>();
    private static List<Appointment> appointments = new ArrayList<>();
    private static List<Prescription> prescriptions = new ArrayList<>();

    public static void main(String[] args) {
        ClinicService.get();

        for (int i = 0; i < 5; i++) {
            Doctor doctor = new Doctor();
            doctor.id = (long) (i + 1);
            doctor.firstName = getRandom(FIRST_NAMES);
            doctor.lastName = getRandom(LAST_NAMES);
            doctor.email = doctor.firstName.toLowerCase() + "." + doctor.lastName.toLowerCase()
                    + "@ensam-ensam-clinic.com";
            doctor.username = doctor.firstName.toLowerCase() + "." + doctor.lastName.toLowerCase();
            doctor.password = "111";
            doctor.specialty = getRandom(SPECIALTIES);

            UserService.saveDoctor(doctor);
            doctors.add(doctor);
        }

        for (int i = 0; i < 5; i++) {
            Assistant assistant = new Assistant();
            assistant.id = (long) (i + doctors.size() + 1);
            assistant.firstName = getRandom(FIRST_NAMES);
            assistant.lastName = getRandom(LAST_NAMES);
            assistant.email = assistant.firstName.toLowerCase() + "." + assistant.lastName.toLowerCase()
                    + "@ensam-clinic.com";
            assistant.username = assistant.firstName.toLowerCase() + "." + assistant.lastName.toLowerCase();
            assistant.password = "111";

            UserService.saveAssistant(assistant);
            assistants.add(assistant);
        }

        for (int i = 0; i < 10; i++) {
            Patient patient = new Patient();
            patient.id = (long) (i + 1);
            patient.firstName = getRandom(FIRST_NAMES);
            patient.lastName = getRandom(LAST_NAMES);
            patient.email = patient.firstName.toLowerCase() + "." + patient.lastName.toLowerCase() + "@gmail.com";
            patient.phone = "0600-" + String.format("%06d", i + 1);
            patient.dateOfBirth = DateUtils.parseDate(getRandom(BIRTH_YEARS) + "-" +
                    String.format("%02d", ThreadLocalRandom.current().nextInt(1, 13)) + "-" +
                    String.format("%02d", ThreadLocalRandom.current().nextInt(1, 29)));
            patient.gender = getRandom(GENDERS);
            patient.medicalRecord = new MedicalRecord(patient);

            PatientService.save(patient);
            patients.add(patient);
        }

        Date today = new Date();
        for (int i = 0; i < 15; i++) {
            Appointment appointment = new Appointment();
            appointment.id = (long) (i + 1);
            appointment.date = today;
            appointment.hour = getRandom(APPOINTMENT_HOURS);
            appointment.patient = getRandom(patients);
            appointment.doctor = getRandom(doctors);
            appointment.notes = "";
            appointment.completed = ThreadLocalRandom.current().nextBoolean();

            AppointmentService.save(appointment);
            appointments.add(appointment);
        }

        for (int i = 0; i < 10; i++) {
            Prescription prescription = new Prescription();
            prescription.id = (long) (i + 1);
            prescription.creationDate = new Date();
            prescription.medication = getRandom(MEDICATIONS);
            prescription.description = "";
            prescription.doctor = getRandom(doctors);
            prescription.patient = getRandom(patients);

            PrescriptionService.save(prescription);
            prescriptions.add(prescription);
        }
    }

    private static <T> T getRandom(T[] array) {
        return array[ThreadLocalRandom.current().nextInt(array.length)];
    }

    private static <T> T getRandom(List<T> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    private static int getRandom(int[] array) {
        return array[ThreadLocalRandom.current().nextInt(array.length)];
    }
}