package fr.jbrenier.petfoodingcontrol.utils;

import org.mindrot.jbcrypt.BCrypt;

import io.reactivex.Completable;

/**
 * Utils for cryptographic manipulations, used for passwords...
 */
public final class CryptographyUtils {

    private static final int rounds = 12;

    private CryptographyUtils() {}

    /**
     * Hash (with BCrypt) the string representing the password entered.
     * @param typedPassword the password entered
     * @return the hashed string
     */
    public static String hashPassword(String typedPassword) {
        return BCrypt.hashpw(typedPassword, BCrypt.gensalt(rounds));
    }

    /**
     * Check if the password entered and the stored hash value match.
     * @param passwordToCheck the passvord entered
     * @param hashedValue the hased password stored
     * @return a Completable that complete in case of success
     */
    public static Completable checkPassword(String passwordToCheck, String hashedValue) {
        if (BCrypt.checkpw(passwordToCheck, hashedValue)) {
            return Completable.complete();
        }
        return Completable.error(new Exception("Passwords mismatch"));
    }
}
