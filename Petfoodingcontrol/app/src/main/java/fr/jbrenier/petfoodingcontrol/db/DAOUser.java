package fr.jbrenier.petfoodingcontrol.db;

import fr.jbrenier.petfoodingcontrol.domain.user.User;

/**
 * DAO interface for User's data access.
 * @author Jérôme Brenier
 */
public interface DAOUser {
    User getByCredentials(String email, String password);
    String getUserPasswd(String email);
    boolean verifyPassword(String passwordToCheck, String storedPassword);
}
