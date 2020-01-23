package fr.jbrenier.petfoodingcontrol.repository;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.user.AutoLogin;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface PetFoodingControlRepository {
    /** User */
    MutableLiveData<User> getUserLogged();
    void setUserLogged(User user);
    Single<User> getUserByEmail(String email);
    Single<User> getUserById(Long userId);
    Single<Photo> getUserPhoto(User user);
    Single<Long> save(User user);
    Completable update(User user);

    /** AutoLogin */
    Single<User> getUserByAutoLogin(String autoLoginToken);
    Completable insert (AutoLogin autoLogin);

    /** Photo */
    Single<Long> save(Photo photo);
    Completable update(Photo photo);

    /** Pet */
    MediatorLiveData<List<Pet>> getUserPets();
    void setUserPets(List<Pet> petList);
    void addUserPet(Pet pet);
    void refreshUserPet();
    void removeUserPet(Pet pet);
    Single<Pet> getPetById(Long petId);
    Single<Photo> getPetPhoto(Pet pet);
    Flowable<List<Pet>> getPetOwnedbyUserId (Long userId);
    Flowable<List<Pet>> getPetsforFeeder(Long userId);
}
