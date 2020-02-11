package fr.jbrenier.petfoodingcontrol.repository;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.PetFeeders;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.food.Fooding;
import fr.jbrenier.petfoodingcontrol.domain.entities.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.AutoLogin;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import fr.jbrenier.petfoodingcontrol.domain.model.Feeder;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface PetFoodingControlRepository {

    /* User */
    Single<User> getUserByEmail(String email);
    Single<User> getUserById(Long userId);
    Single<Photo> getUserPhoto(User user);
    Single<Long> save(User user);
    Completable update(User user);

    /* AutoLogin */
    Single<User> getUserByAutoLogin(String autoLoginToken);
    Completable insert (AutoLogin autoLogin);

    /* Photo */
    Single<Long> save(Photo photo);
    Completable update(Photo photo);

    /* Pet */
    Single<Long> save(Pet pet);
    Completable update(Pet pet);
    Single<Pet> getPetById(Long petId);
    Single<Photo> getPetPhoto(Pet pet);
    Single<Feeder> getFeederByEmail(String feederEmail);
    Flowable<List<Pet>> getAllUserPetsByUserId(Long userId);
    Single<Long> insert(PetFeeders petFeeders);
    Completable insert(List<PetFeeders> petFeedersList);

    /* Fooding */
    Flowable<List<Fooding>> getFoodingsForPet (Long petId);
    Completable insert(Fooding fooding);
}
