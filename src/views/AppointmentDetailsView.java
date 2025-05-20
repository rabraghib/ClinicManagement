package views;

import models.Appointment;
import services.AppointmentService;
import utils.ConsoleUtils;
import utils.DateUtils;

import java.util.Date;

public class AppointmentDetailsView {
    private final Appointment appointment;

    public AppointmentDetailsView(Appointment appointment) {
        this.appointment = appointment;
    }

    public void show() {
        while (true) {
            ConsoleUtils.clearScreen();
            ConsoleUtils.printTitle("Appointment Details");

            ConsoleUtils.printModelInfo(appointment.toKeyValueMap());

            System.out.println("\nPlease select an option:");
            System.out.println("1. Edit");
            System.out.println("2. Cancel");
            System.out.println("3. Mark Appointment");
            System.out.println("4. View Patient");
            System.out.println("5. Back");

            int choice = ConsoleUtils.readInt("\nEnter your choice (1-5): ", 1, 5);

            switch (choice) {
                case 1 -> editAppointment();
                case 2 -> {
                    if (cancelAppointment()) {
                        return;
                    }
                }
                case 3 -> markAppointment();
                case 4 -> viewPatient();
                case 5 -> {
                    return;
                }
            }
        }
    }

    private void editAppointment() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Edit Appointment");

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

    private boolean cancelAppointment() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Cancel Appointment");

        System.out.println("Are you sure you want to cancel this appointment? (y/n)");
        String confirm = ConsoleUtils.readLine().toLowerCase();
        if (!confirm.equals("y")) {
            return false;
        }

        AppointmentService.remove(appointment.id);
        System.out.println("\nAppointment cancelled successfully!");
        ConsoleUtils.waitForEnter();
        return true;
    }

    private void markAppointment() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printTitle("Mark Appointment");

        if (appointment.completed) {
            System.out.println("This appointment is already completed.");
            ConsoleUtils.waitForEnter();
            return;
        }

        System.out.println();
        final boolean confirm = ConsoleUtils.readBool("Mark appointment as completed? (y/n)");
        if (!confirm) {
            ConsoleUtils.waitForEnter();
            return;
        }

        appointment.completed = true;
        AppointmentService.save(appointment);
        System.out.println("\nAppointment marked as completed!");
        ConsoleUtils.waitForEnter();
    }

    private void viewPatient() {
        new PatientDetailsView(appointment.patient).show();
    }
}