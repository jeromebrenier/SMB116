package fr.jbrenier.petfoodingcontrol.utils;

import android.util.Log;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Utils for manipulating dates and time.
 * @author Jérôme Brenier
 */
public final class DateTimeUtils {

    private DateTimeUtils() {}

    /**
     * Convert the string representing a date in the format dd/MM/yy into an OffsetDateTime with
     * a time set at 00:00:00.
     * @param date_dd_MM_yy the date as a string
     * @return the OffsetDateTime result of the conversion
     */
    public static OffsetDateTime getOffsetDateTimeFromBirthDate(String date_dd_MM_yy) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy-HH:mm:ss");
        LocalDateTime datetime = LocalDateTime.parse(date_dd_MM_yy + "-00:00:00", formatter);
        ZonedDateTime zoned = datetime.atZone(TimeZone.getDefault().toZoneId());
        return zoned.toOffsetDateTime();
    }

    /**
     * Convert an OffsetDateTime into a string in the format dd/MM/yy.
     * @param offsetDateTime the OffsetDateTime to convert
     * @return the string representation of the OffsetDateTime
     */
    public static String getStringBirthDateFromOffsetDateTime(OffsetDateTime offsetDateTime) {
        String result = offsetDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yy"));
        return offsetDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yy"));
    }
}
