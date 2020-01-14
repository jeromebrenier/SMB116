package fr.jbrenier.petfoodingcontrol.repository;

import androidx.lifecycle.MutableLiveData;

import fr.jbrenier.petfoodingcontrol.domain.user.User;

public interface UserRepository {
    /**
     * Get the User corresponding to given credentials (email and password). Return null if
     * credentials invalid.
     *
     * @param email    of the user
     * @param password of the user
     * @return the User
     */
    public User getByCredentials(String email, String password);
    public String getUserPasswd(String email);
    public boolean verifyPassword(String passwordToCheck, String storedPassword);
    public void setUserLogged(User user);
    public MutableLiveData<User> getUserLogged();
}
