package views;

import models.Appointment;
import models.Doctor;
import models.Patient;
import services.AppointmentService;
import services.UserService;
import utils.ConsoleUtils;
import utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AppointmentView {
    public void show() {
        while (true) {
            ConsoleUtils.clearScreen();
            ConsoleUtils.printTitle("Appointments");

            System.out.println("\nPlease select an option:");
            System.out.println("1. View All");
            System.out.println("2. View Today");
            System.out.println("3. View This Week");
            System.out.println("4. View This Month");
            System.out.println("5. Back");

            int choice = ConsoleUtils.readInt("\nEnter your choice (1-5): ", 1, 5);

            switch (choice) {
                case 1 -> viewAllAppointments();
                case 2 -> viewTodayAppointments();
                case 3 -> viewWeekAppointments();
                case 4 -> viewMonthAppointments();
                case 5 -> {
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

        showAppointmentsList(appointments);
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

        showAppointmentsList(appointments);
    }

    private void viewWeekAppointments() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("This Week's Appointments");

        Date today = new Date();
        Calendar startDate = DateUtils.startOfDay(today);
        Calendar endDate = DateUtils.endOfDay(today);
        endDate.add(Calendar.DAY_OF_WEEK, 7);

        List<Appointment> appointments = AppointmentService.getByDateRange(startDate.getTime(),
                endDate.getTime());
        if (appointments.isEmpty()) {
            System.out.println("No appointments scheduled for this week.");
            ConsoleUtils.waitForEnter();
            return;
        }

        showAppointmentsList(appointments);
    }

    private void viewMonthAppointments() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("This Month's Appointments");

        Date today = new Date();
        Calendar startDate = DateUtils.startOfDay(today);
        Calendar endDate = DateUtils.endOfDay(today);
        startDate.set(Calendar.DAY_OF_MONTH, 1);
        endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        List<Appointment> appointments = AppointmentService.getByDateRange(startDate.getTime(), endDate.getTime());
        if (appointments.isEmpty()) {
            System.out.println("No appointments scheduled for this month.");
            ConsoleUtils.waitForEnter();
            return;
        }

        showAppointmentsList(appointments);
    }

    private void showAppointmentsList(List<Appointment> appointments) {
        List<Map<String, String>> appointmentsData = appointments.stream()
                .map(Appointment::toKeyValueMap)
                .toList();

        ConsoleUtils.printModelList(appointmentsData);

        int appointmentIndex = ConsoleUtils.readInt("\nEnter appointment number to view details (0 to go back): ", 0,
                appointments.size()) - 1;
        if (appointmentIndex == -1)
            return;

        Appointment appointment = appointments.get(appointmentIndex);
        new AppointmentDetailsView(appointment).show();
    }

    public void scheduleAppointment(Patient patient) {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Schedule New Appointment");

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
}