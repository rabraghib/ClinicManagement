package models;

import utils.StringUtils;
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
        return super.toFileString();
    }

    public static Assistant fromFileString(String str) {
        String[] parts = StringUtils.fileStringToList(str);
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
    public String toViewListString() {
        return super.toViewListString() + " (Assistant)";
    }

    @Override
    public Map<String, String> toKeyValueMap() {
        Map<String, String> map = super.toKeyValueMap();
        map.put("Role", "Assistant");
        return map;
    }
}