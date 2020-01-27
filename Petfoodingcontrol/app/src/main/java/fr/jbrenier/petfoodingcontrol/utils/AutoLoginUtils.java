package fr.jbrenier.petfoodingcontrol.utils;

import java.util.UUID;

/**
 * Utils for AutoLogin.
 */
public final class AutoLoginUtils {
    private static AutoLoginUtils INSTANCE;
    private UUID uuid;

    private AutoLoginUtils() {
        uuid = UUID.randomUUID();
    }

    public static AutoLoginUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutoLoginUtils();
        }
        return INSTANCE;
    }

    public UUID getUuid() {
        return uuid;
    }
}
