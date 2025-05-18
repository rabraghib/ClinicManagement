package models;

import java.util.regex.Pattern;
import utils.Constants;
import java.util.Map;

public class Assistant extends User {
    public Assistant() {
        super();
    }

    public Assistant(Long id, String firstName, String lastName, String email, String username, String password) {
        super(id, firstName, lastName, email, username, password);
    }

    @Override
    public String toFileString() {
        return id + Constants.FILE_SEPARATOR +
                firstName + Constants.FILE_SEPARATOR +
                lastName + Constants.FILE_SEPARATOR +
                email + Constants.FILE_SEPARATOR +
                username + Constants.FILE_SEPARATOR +
                password;
    }

    public static Assistant fromFileString(String str) {
        String[] parts = str.split(Pattern.quote(Constants.FILE_SEPARATOR));
        if (parts.length >= 6) {
            Assistant assistant = new Assistant();
            assistant.id = Long.parseLong(parts[0]);
            assistant.firstName = parts[1];
            assistant.lastName = parts[2];
            assistant.email = parts[3];
            assistant.username = parts[4];
            assistant.password = parts[5];
            return assistant;
        }
        return null;
    }

    @Override
    public Map<String, String> toKeyValueMap() {
        Map<String, String> map = super.toKeyValueMap();
        map.put("Role", "Assistant");
        return map;
    }
}