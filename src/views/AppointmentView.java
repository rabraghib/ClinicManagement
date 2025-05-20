package views;

import models.Appointment;
import models.Doctor;
import models.Patient;
import services.AppointmentService;
import services.UserService;
import services.PatientService;
import utils.ConsoleUtils;
import utils.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class AppointmentView {
    public void show() {
        while (true) {
            ConsoleUtils.clearScreen();
            ConsoleUtils.printTitle("Appointment Management");

            System.out.println("\nPlease select an option:");
            System.out.println("1. View All Appointments");
            System.out.println("2. View Today's Appointments");
            System.out.println("3. Schedule New Appointment");
            System.out.println("4. Edit Appointment");
            System.out.println("5. Cancel Appointment");
            System.out.println("6. Mark Appointment as Completed");
            System.out.println("7. Back to Main Menu");

            int choice = ConsoleUtils.readInt("\nEnter your choice (1-7): ", 1, 7);

            switch (choice) {
                case 1 -> viewAllAppointments();
                case 2 -> viewTodayAppointments();
                case 3 -> scheduleAppointment();
                case 4 -> editAppointment();
                case 5 -> cancelAppointment();
                case 6 -> completeAppointment();
                case 7 -> {
                    return;
                }
            }
        }
    }

    private void viewAllAppointments() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("All Appointments");

        List<Appointment> appointments = AppointmentService.getAll();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> appointmentsData = appointments.stream()
                .map(Appointment::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(appointmentsData);
        ConsoleUtils.waitForEnter();
    }

    private void viewTodayAppointments() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Today's Appointments");

        List<Appointment> appointments = AppointmentService.getToday();
        if (appointments.isEmpty()) {
            System.out.println("No appointments scheduled for today.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> appointmentsData = appointments.stream()
                .map(Appointment::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(appointmentsData);
        ConsoleUtils.waitForEnter();
    }

    private void scheduleAppointment() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Schedule New Appointment");

        List<Patient> patients = PatientService.getAll();
        if (patients.isEmpty()) {
            System.out.println("No patients available.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> patientsData = patients.stream()
                .map(Patient::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(patientsData);

        int patientIndex = ConsoleUtils.readInt("\nSelect patient (0 to cancel): ", 0, patients.size()) - 1;
        if (patientIndex == -1)
            return;

        Patient patient = patients.get(patientIndex);

        List<Doctor> doctors = UserService.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("No doctors available.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> doctorsData = doctors.stream()
                .map(Doctor::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(doctorsData);

        int doctorIndex = ConsoleUtils.readInt("\nSelect doctor (0 to cancel): ", 0, doctors.size()) - 1;
        if (doctorIndex == -1)
            return;

        Doctor doctor = doctors.get(doctorIndex);

        System.out.print("\nDate (YYYY-MM-DD): ");
        Date date = DateUtils.parseDate(ConsoleUtils.readLine());
        if (date == null) {
            System.out.println("Invalid date format.");
            ConsoleUtils.waitForEnter();
            return;
        }

        System.out.print("Hour (0-23): ");
        int hour = ConsoleUtils.readInt("", 0, 23);

        if (AppointmentService.isTimeSlotBooked(date, hour, doctor.id)) {
            System.out.println("\nThis time slot is already booked.");
            ConsoleUtils.waitForEnter();
            return;
        }

        Appointment appointment = AppointmentService.create(date, hour, patient.id, doctor.id);
        if (appointment == null) {
            System.out.println("\nFailed to create appointment.");
            ConsoleUtils.waitForEnter();
            return;
        }

        System.out.println("\nAppointment scheduled successfully!");
        ConsoleUtils.waitForEnter();
    }

    private void editAppointment() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Edit Appointment");

        List<Appointment> appointments = AppointmentService.getAll();
        if (appointments.isEmpty()) {
            System.out.println("No appointments available to edit.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> appointmentsData = appointments.stream()
                .map(Appointment::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(appointmentsData);

        int appointmentIndex = ConsoleUtils.readInt("\nEnter appointment number to edit (0 to cancel): ", 0,
                appointments.size()) - 1;
        if (appointmentIndex == -1)
            return;

        Appointment appointment = appointments.get(appointmentIndex);

        System.out.println("\nEnter new information (press Enter to keep current value):");

        System.out.print("Date (YYYY-MM-DD) [" + DateUtils.formatDate(appointment.date) + "]: ");
        String dateStr = ConsoleUtils.readLine();
        Date date = null;
        if (!dateStr.isEmpty()) {
            date = DateUtils.parseDate(dateStr);
            if (date == null) {
                System.out.println("Invalid date format.");
                ConsoleUtils.waitForEnter();
                return;
            }
        }

        System.out.print("Hour (0-23) [" + appointment.hour + "]: ");
        String hourStr = ConsoleUtils.readLine();

        if (!hourStr.isEmpty()) {
            try {
                int hour = Integer.parseInt(hourStr);
                if (hour >= 0 && hour <= 23)
                    appointment.hour = hour;
            } catch (NumberFormatException e) {
                System.out.println("Invalid hour format.");
            }
        }

        System.out.print("Notes [" + appointment.notes + "]: ");
        String notes = ConsoleUtils.readLine();
        if (!notes.isEmpty())
            appointment.notes = notes;

        AppointmentService.save(appointment);

        System.out.println("\nAppointment updated successfully!");
        ConsoleUtils.waitForEnter();
    }

    private void cancelAppointment() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Cancel Appointment");

        List<Appointment> appointments = AppointmentService.getAll();
        if (appointments.isEmpty()) {
            System.out.println("No appointments available to cancel.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> appointmentsData = appointments.stream()
                .map(Appointment::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(appointmentsData);

        int appointmentIndex = ConsoleUtils.readInt("\nEnter appointment number to cancel (0 to cancel): ", 0,
                appointments.size()) - 1;
        if (appointmentIndex == -1)
            return;

        Appointment appointment = appointments.get(appointmentIndex);
        AppointmentService.remove(appointment.id);

        System.out.println("\nAppointment cancelled successfully!");
        ConsoleUtils.waitForEnter();
    }

    private void completeAppointment() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Complete Appointment");

        List<Appointment> appointments = AppointmentService.getPending();
        if (appointments.isEmpty()) {
            System.out.println("No pending appointments available.");
            ConsoleUtils.waitForEnter();
            return;
        }

        List<Map<String, String>> appointmentsData = appointments.stream()
                .map(Appointment::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(appointmentsData);

        int appointmentIndex = ConsoleUtils.readInt("\nEnter appointment number to mark as completed (0 to cancel): ",
                0, appointments.size()) - 1;
        if (appointmentIndex == -1)
            return;

        Appointment appointment = appointments.get(appointmentIndex);
        appointment.completed = true;
        AppointmentService.save(appointment);

        System.out.println("\nAppointment marked as completed!");
        ConsoleUtils.waitForEnter();
    }
}