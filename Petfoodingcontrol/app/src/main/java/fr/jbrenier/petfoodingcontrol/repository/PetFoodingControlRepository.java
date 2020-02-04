package fr.jbrenier.petfoodingcontrol.repository;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.entities.photo.Photo;
import fr.jbrenier.petfoodingcontrol.entities.user.AutoLogin;
import fr.jbrenier.petfoodingcontrol.entities.user.User;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface PetFoodingControlRepository {
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
    Single<Long> save(Pet pet);
    Completable update(Pet pet);
    Single<Pet> getPetById(Long petId);
    Single<Photo> getPetPhoto(Pet pet);
    Flowable<List<Pet>> getAllUserPetsByUserId(Long userId);
}
