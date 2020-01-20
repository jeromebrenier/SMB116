package fr.jbrenier.petfoodingcontrol.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.user.AutoLogin;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface PetFoodingControlRepository {
    /** User */
    Single<User> getUserByEmail(String email);
    Single<User> getUserById(Long userId);
    Single<Photo> getUserPhoto(User user);
    void setUserLogged(User user);
    MutableLiveData<User> getUserLogged();
    Single<Long> save(User user);
    Completable update(User user);

    /** AutoLogin */
    Single<User> getUserByAutoLogin(String autoLoginToken);
    Completable insert (AutoLogin autoLogin);

    /** Photo */
    Single<Long> save(Photo photo);

    /** Pet */
    Single<Pet> getPetById(Long petId);
    void setUserPets(User user);
    Single<Photo> getPetPhoto(Pet pet);
    MutableLiveData<List<Pet>> getUserPets();
}
