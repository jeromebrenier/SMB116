package fr.jbrenier.petfoodingcontrol.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.user.User;

public interface PetFoodingControlRepository {
    /**
     * Get the User corresponding to given credentials (email and password). Return null if
     * credentials invalid.
     *
     * @param email    of the user
     * @param password of the user
     * @return the User
     */
    public User getUserByCredentials(String email, String password);
    public Photo getUserPhoto(User user);
    public String getUserPasswd(String email);
    public boolean verifyUserPassword(String passwordToCheck, String storedPassword);
    public void setUserLogged(User user);
    public MutableLiveData<User> getUserLogged();

    public Pet getPetById(String id);
    public void setUserPets(User user);
    public MutableLiveData<List<Pet>> getUserPets();
}
