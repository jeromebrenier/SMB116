package fr.jbrenier.petfoodingcontrol.utils;

import java.util.regex.Pattern;

/**
 * Utility class for text input validation.
 * @author Jérôme Brenier
 */
public final class InputValidationUtils {

    private static final String emailRegex = "^(.+)@(.+)$";
    private static final String dateRegex =
            "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/([0-9]{2})$";
            //"^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{2}$";

    private InputValidationUtils() {}

    /**
     * Check if the string given is a valid email.
     * @param email the email as a string
     * @return true if valid, false otherwise
     */
    public static boolean isEmailValid(final String email) {
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    /**
     * Check if the string representing a date is valid, ie matches the regex.
     * @param date the date to check
     * @return the result, true = valid, false otherwise
     */
    public static boolean isDateValid(final String date) {
        Pattern pattern = Pattern.compile(dateRegex);
        return pattern.matcher(date).matches();
    }

}
