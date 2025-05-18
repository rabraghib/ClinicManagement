package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    static SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_PATTERN);

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return dateFormat.format(date);
    }

    public static Date parseDate(String dateString) {
        try {
            if (dateString != null && !dateString.isEmpty()) {
                return dateFormat.parse(dateString);
            }
        } catch (ParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
        }
        return null;
    }
}
