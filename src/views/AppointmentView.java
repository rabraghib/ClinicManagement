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

        List<Appointment> appointments = AppointmentService.getAllAppointments();
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

        List<Appointment> appointments = AppointmentService.getTodayAppointments();
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

        // Select Patient
        List<Patient> patients = PatientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients available. Please add a patient first.");
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

        // Select Doctor
        List<Doctor> doctors = UserService.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("No doctors available. Please add a doctor first.");
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

        // Get Date and Time
        System.out.print("\nEnter date (YYYY-MM-DD): ");
        Date date = DateUtils.parseDate(ConsoleUtils.readLine());
        if (date == null) {
            System.out.println("Invalid date format.");
            ConsoleUtils.waitForEnter();
            return;
        }

        System.out.print("Enter hour (0-23): ");
        int hour = ConsoleUtils.readInt("", 0, 23);

        System.out.print("Enter notes (optional): ");
        String notes = ConsoleUtils.readLine();

        Appointment appointment = new Appointment(null, date, hour, patient, doctor);
        appointment.notes = notes;
        AppointmentService.saveAppointment(appointment);

        System.out.println("\nAppointment scheduled successfully!");
        ConsoleUtils.waitForEnter();
    }

    private void editAppointment() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Edit Appointment");

        List<Appointment> appointments = AppointmentService.getAllAppointments();
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

        System.out.print("Date [" + appointment.date + "] (YYYY-MM-DD): ");
        String dateStr = ConsoleUtils.readLine();
        if (!dateStr.isEmpty()) {
            Date date = DateUtils.parseDate(dateStr);
            if (date != null)
                appointment.date = date;
        }

        System.out.print("Hour [" + appointment.hour + "]: ");
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

        AppointmentService.saveAppointment(appointment);

        System.out.println("\nAppointment updated successfully!");
        ConsoleUtils.waitForEnter();
    }

    private void cancelAppointment() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Cancel Appointment");

        List<Appointment> appointments = AppointmentService.getAllAppointments();
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
        AppointmentService.removeAppointment(appointment.id);

        System.out.println("\nAppointment cancelled successfully!");
        ConsoleUtils.waitForEnter();
    }

    private void completeAppointment() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Complete Appointment");

        List<Appointment> appointments = AppointmentService.getPendingAppointments();
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
        AppointmentService.saveAppointment(appointment);

        System.out.println("\nAppointment marked as completed!");
        ConsoleUtils.waitForEnter();
    }
}