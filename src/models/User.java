package models;

import utils.StringUtils;

import java.util.Map;
import java.util.LinkedHashMap;

public abstract class User extends SerializableModel {
    public String firstName;
    public String lastName;
    public String email;
    public String username;
    public String password;

    public User() {
    }

    public User(Long id, String firstName, String lastName, String email, String username, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toFileString() {
        return StringUtils.toFileString(
                String.valueOf(id),
                firstName,
                lastName,
                email,
                username,
                password);
    }

    public Map<String, String> toKeyValueMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("ID", String.valueOf(id));
        map.put("First Name", firstName);
        map.put("Last Name", lastName);
        map.put("Email", email);
        map.put("Username", username);
        return map;
    }
}