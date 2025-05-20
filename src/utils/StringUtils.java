package utils;

import java.util.regex.Pattern;

public class StringUtils {

    public static String toFileString(String... strings) {
        if (strings == null || strings.length == 0) {
            return "";
        }
        for (int i = 0; i < strings.length; i++) {
            if (strings[i] == null || strings[i].trim().isEmpty()) {
                strings[i] = " ";
            }
        }
        return String.join(Constants.FILE_SEPARATOR, strings);
    }

    public static String[] fileStringToList(String fileString) {
        if (fileString == null || fileString.trim().isEmpty()) {
            return new String[0];
        }
        String[] result = fileString.split(Pattern.quote(Constants.FILE_SEPARATOR));
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i].trim();
        }
        return result;
    }
}