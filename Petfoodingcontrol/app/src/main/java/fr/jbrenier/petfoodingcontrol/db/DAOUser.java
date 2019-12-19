package fr.jbrenier.petfoodingcontrol.db;

import fr.jbrenier.petfoodingcontrol.domain.user.User;

/**
 * Class for getting data about User.
 * @author Jérôme Brenier
 */
public class DAOUser {

    /**
     * Get the User corresponding to given credentials (email and password). Return null if
     * credentials invalid.
     * @param email of the user
     * @param password of the user
     * @return the User
     */
    public static User getByCredentials(String email, String password) {
        String storedPassword = getUserPasswd(email);
        if (storedPassword != null && verifyPassword(password, storedPassword)) {
            User test = new User("idTest", "Test", "test@test.fr", "password");
            return test;
        }
        return null;
    }

    private static String getUserPasswd(String email) {
        return "test";
    }

    private static boolean verifyPassword(String passwordToCheck, String storedPassword) {
        return true;
    }
}
