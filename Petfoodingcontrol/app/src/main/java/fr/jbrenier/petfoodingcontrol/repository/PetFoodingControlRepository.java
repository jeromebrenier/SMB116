package fr.jbrenier.petfoodingcontrol.repository;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.PetFeeder;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.food.Fooding;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.weight.Weighing;
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
    Single<Long> saveUser(User user);
    Completable updateUser(User user);

    /* AutoLogin */
    Single<User> getUserByAutoLogin(String autoLoginToken);
    Completable insertAutoLogin(AutoLogin autoLogin);

    /* Photo */
    Single<Long> savePhoto(Photo photo);
    Completable updatePhoto(Photo photo);

    /* Pet */
    Single<Long> savePet(Pet pet);
    Completable deletePet(Pet pet);
    Completable updatePet(Pet pet);
    Single<Pet> getPetById(Long petId);
    Single<Photo> getPetPhoto(Pet pet);
    Single<Feeder> getFeederByEmail(String feederEmail);
    Flowable<List<Pet>> getAllUserPetsByUserId(Long userId);
    Single<Long> insertPetFeeder(PetFeeder petFeeder);
    Completable insertPetFeeders(List<PetFeeder> petFeederList);

    /* Fooding */
    Flowable<List<Fooding>> getFoodingsForPet(Long petId);
    Flowable<List<Fooding>> getDailyFoodingsForPet(Long petId);
    Completable insertFooding(Fooding fooding);

    /* Weighing */
    Flowable<List<Weighing>> getWeighingsForPet(Long petId);
    Completable insertWeighing(Weighing weighing);
}
