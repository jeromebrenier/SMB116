package fr.jbrenier.petfoodingcontrol.utils;

import android.provider.Settings;

import org.mindrot.jbcrypt.BCrypt;

import io.reactivex.Completable;

/**
 * Utils for cryptographic manipulations, used for passwords...
 */
public final class CryptographyUtils {

    private static final int rounds = 16;

    private CryptographyUtils() {}

    public static String hashPassword(String typedPassword) {
        return BCrypt.hashpw(typedPassword, BCrypt.gensalt(12));
    }

    public static Completable checkPassword(String passwordToCheck, String hashedValue) {
        if (BCrypt.checkpw(passwordToCheck, hashedValue)) {
            return Completable.complete();
        }
        return Completable.error(new Exception("Passwords mismatch"));
    }
}
