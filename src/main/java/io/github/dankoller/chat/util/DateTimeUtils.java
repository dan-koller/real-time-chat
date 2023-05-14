package io.github.dankoller.chat.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    /**
     * This method is used to convert a LocalDateTime object to a String. The format of the input string is
     * dd.MM.yyyy HH:mm and the days and months can be one or two digits.
     * In order to convert the string properly, the input string is first parsed to a LocalDateTime object and then
     * formatted to the desired format.
     * The output can then be used to compare the LocalDateTime objects.
     *
     * @param dateTime The LocalDateTime object to convert
     * @return The LocalDateTime object of the input string
     */
    public static LocalDateTime fromString(String dateTime) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d[.][d]M[.][uuuu] H[:][mm]");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime.trim(), inputFormatter);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return LocalDateTime.parse(localDateTime.format(outputFormatter), outputFormatter);
    }
}
