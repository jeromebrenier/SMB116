package fr.jbrenier.petfoodingcontrol.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import io.reactivex.Completable;
import io.reactivex.Single;

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
    public boolean checkUserExistance(User user);
    public Single<Photo> getUserPhoto(User user);
    public String getUserPasswd(String email);
    public boolean verifyUserPassword(String passwordToCheck, String storedPassword);
    public void setUserLogged(User user);
    public MutableLiveData<User> getUserLogged();
    public Completable save(User user);

    public void save(Photo photo);

    public Pet getPetById(String id);
    public void setUserPets(User user);
    public Single<Photo> getPetPhoto(Pet pet);
    public MutableLiveData<List<Pet>> getUserPets();
}
