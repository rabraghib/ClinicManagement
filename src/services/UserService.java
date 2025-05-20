package services;

import models.Doctor;
import models.Assistant;
import models.User;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class UserService {

    private static final DataRepository<Doctor> doctorRepo = new DataRepository<>("data/doctors.txt",
            Doctor::fromFileString);
    private static final DataRepository<Assistant> assistantRepo = new DataRepository<>("data/assistants.txt",
            Assistant::fromFileString);

    public static List<User> getAll() {
        List<User> users = new ArrayList<>();
        users.addAll(doctorRepo.loadAll());
        users.addAll(assistantRepo.loadAll());
        return users;
    }

    public static User login(String username, String password) {
        List<User> users = getAll();
        for (User user : users) {
            System.out.println("Username=" + user.username + " Password=" + user.password);
            if (user.username.equals(username) && user.password.equals(password)) {
                return user;
            }
        }
        return null;
    }

    public static Doctor saveDoctor(Doctor doctor) {
        return doctorRepo.save(doctor);
    }

    public static Assistant saveAssistant(Assistant assistant) {
        return assistantRepo.save(assistant);
    }

    public static Doctor getDoctorById(Long id) {
        return doctorRepo.loadById(id);
    }

    public static Assistant getAssistantById(Long id) {
        return assistantRepo.loadById(id);
    }

    public static List<Doctor> getAllDoctors() {
        return doctorRepo.loadAll();
    }

    public static List<Assistant> getAllAssistants() {
        return assistantRepo.loadAll();
    }

    public static boolean removeDoctor(Long id) {
        return doctorRepo.remove(id);
    }

    public static boolean removeAssistant(Long id) {
        return assistantRepo.remove(id);
    }

    public static List<User> find(Predicate<User> predicate) {
        List<User> result = new ArrayList<>();
        for (User user : getAll()) {
            if (predicate.test(user)) {
                result.add(user);
            }
        }
        return result;
    }

    public static boolean isUsernameTaken(String username) {
        return !find(user -> user.username.equalsIgnoreCase(username)).isEmpty();
    }
}