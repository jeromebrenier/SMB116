package fr.jbrenier.petfoodingcontrol.repository;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import java.util.List;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.db.PetFoodingControlDatabase;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * The Pet Fooding Control Repository implementation.
 * @author Jérôme Brenier
 */
public class PetFoodingControlRepositoryDaoImpl implements PetFoodingControlRepository {

    /** LOGGING */
    private static final String TAG = "PetFoodingControlRepositoryDaoImpl";

    private static final String DB_NAME = "pfc_db";
    private final PetFoodingControlDatabase petFoodingControlDatabase;

    @Inject
    public PetFoodingControlRepositoryDaoImpl(Application application) {
        petFoodingControlDatabase = Room.databaseBuilder(application, PetFoodingControlDatabase.class, DB_NAME).build();
        Log.d(TAG, "PetFoodingControlRepositoryDaoImpl instantiation");
    }

    @Override
    public Single<User> getUserByEmail(String email) {
        Log.d(TAG, "getUserByEmail(" + email + ")");
        return petFoodingControlDatabase.getUserDao().getUserByEmail(email)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<User> getUserById(Long userId) {
        Log.d(TAG, "getUserById(" + userId + ")");
        return petFoodingControlDatabase.getUserDao().getUserById(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Photo> getUserPhoto(User user) {
        Log.d(TAG, "getUserPhoto(" + user + ")");
        return petFoodingControlDatabase.getPhotoDao().getPhotoById(user.getPhotoId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Long> saveUser(User user) {
        Log.d(TAG, "saveUser(" + user + ")");
        return petFoodingControlDatabase.getUserDao().insert(user)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable updateUser(User user) {
        Log.d(TAG, "updateUser(" + user + ")");
        return petFoodingControlDatabase.getUserDao().update(user)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<User> getUserByAutoLogin(String autoLoginToken) {
        Log.d(TAG, "getUserByAutoLogin(" + autoLoginToken + ")");
        return petFoodingControlDatabase.getUserDao().getUserByAutoLogin(autoLoginToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable insertAutoLogin(AutoLogin autoLogin) {
        Log.d(TAG, "insert(" + autoLogin + ")");
        return petFoodingControlDatabase.getUserDao().insertAutoLogin(autoLogin)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Long> savePhoto(Photo photo) {
        Log.d(TAG, "savePhoto(" + photo + ")");
        return petFoodingControlDatabase.getPhotoDao().insert(photo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable updatePhoto(Photo photo) {
        Log.d(TAG, "updatePhoto(" + photo + ")");
        return petFoodingControlDatabase.getPhotoDao().update(photo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Long> savePet(Pet pet) {
        Log.d(TAG, "savePet(" + pet + ")");
        return petFoodingControlDatabase.getPetDao().insertPet(pet)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable deletePet(Pet pet) {
        Log.d(TAG, "deletePet(" + pet + ")");
        return petFoodingControlDatabase.getPetDao().deletePet(pet)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable updatePet(Pet pet) {
        Log.d(TAG, "updatePet(" + pet + ")");
        return petFoodingControlDatabase.getPetDao().updatePet(pet)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Pet> getPetById(Long petId) {
        Log.d(TAG, "getPetById(" + petId + ")");
        return petFoodingControlDatabase.getPetDao().getPetbyId(petId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Photo> getPetPhoto(Pet pet) {
        Log.d(TAG, "getPetPhoto(" + pet + ")");
        return petFoodingControlDatabase.getPhotoDao().getPhotoById(pet.getPhotoId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Feeder> getFeederByEmail(String feederEmail) {
        Log.d(TAG, "getFeederByEmail(" + feederEmail + ")");
        return petFoodingControlDatabase.getPetDao().getFeederByEmail(feederEmail)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<List<Feeder>> getFeedersForPet(Long petId) {
        Log.d(TAG, "getFeedersForPet(" + petId + ")");
        return petFoodingControlDatabase.getPetDao().getFeedersForPet(petId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Flowable<List<Pet>> getAllUserPetsByUserId(Long userId) {
        Log.d(TAG, "getAllUserPetsByUserId(" + userId + ")");
        return petFoodingControlDatabase.getPetDao().getAllUserPetsByUserId(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Long> insertPetFeeder(PetFeeder petFeeder) {
        Log.d(TAG, "insert PetFeeder "
                + petFeeder.getPetId() + " - " + petFeeder.getUserId());
        return petFoodingControlDatabase.getPetDao().insertPetFeeder(petFeeder)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable insertPetFeeders(List<PetFeeder> petFeederList) {
        Log.d(TAG, "insert List of " + petFeederList.size() + " PetFeeder ");
        return petFoodingControlDatabase.getPetDao().insertPetFeeders(petFeederList)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Flowable<List<Fooding>> getFoodingsForPet(Long petId) {
        Log.d(TAG, "getFoodingsForPet(" + petId + ")");
        return petFoodingControlDatabase.getPetDao().getFoodingsForPet(petId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Flowable<List<Fooding>> getDailyFoodingsForPet(Long petId) {
        Log.d(TAG, "getDailyFoodingsForPet(" + petId + ")");
        return petFoodingControlDatabase.getPetDao().getDailyFoodingsForPet(petId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable insertFooding(Fooding fooding) {
        Log.d(TAG, "insert fooding of " + fooding.getQuantity() + " for pet id ("
                + fooding.getPetId() + ") and user id (" + fooding.getUserId() + ")");
        return petFoodingControlDatabase.getPetDao().insertFooding(fooding)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Flowable<List<Weighing>> getWeighingsForPet(Long petId) {
        Log.d(TAG, "getWeighingsForPet(" + petId + ")");
        return petFoodingControlDatabase.getPetDao().getWeighingsForPet(petId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable insertWeighing(Weighing weighing) {
        Log.d(TAG, "insert weighing of " + weighing.getWeightInGrams()+ " for pet id ("
                + weighing.getPetId() + ")");
        return petFoodingControlDatabase.getPetDao().insertWeighing(weighing)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
