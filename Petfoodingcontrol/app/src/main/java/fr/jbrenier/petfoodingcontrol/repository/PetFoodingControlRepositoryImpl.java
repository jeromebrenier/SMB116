package fr.jbrenier.petfoodingcontrol.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import java.util.List;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.db.PetFoodingControlDatabase;
import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.user.AutoLogin;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * The Pet Fooding Control Repository implementation.
 * @author Jérôme Brenier
 */
public class PetFoodingControlRepositoryImpl implements PetFoodingControlRepository {

    /** LOGGING */
    private static final String TAG = "PetFoodingControlRepositoryImpl";

    private static final String DB_NAME = "pfc_db";
    private final PetFoodingControlDatabase petFoodingControlDatabase;
    private final MutableLiveData<User> userLogged = new MutableLiveData<>();
    private LiveData<List<Pet>> userPets;

    @Inject
    public PetFoodingControlRepositoryImpl(Application application) {
        petFoodingControlDatabase = Room.databaseBuilder(application, PetFoodingControlDatabase.class, DB_NAME).build();
        Log.d(TAG, "PetFoodingControlRepositoryImpl instantiation");
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
    public Single<Long> save(User user) {
        Log.d(TAG, "save(" + user + ")");
        return petFoodingControlDatabase.getUserDao().insert(user)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable update(User user) {
        Log.d(TAG, "update(" + user + ")");
        return petFoodingControlDatabase.getUserDao().update(user)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<User> getUserByAutoLogin(String autoLoginToken) {
        Log.d(TAG, "getUserByAutoLogin(" + autoLoginToken + ")");
        return petFoodingControlDatabase.getAutoLoginDao().getUserByAutoLogin(autoLoginToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable insert(AutoLogin autoLogin) {
        Log.d(TAG, "insert(" + autoLogin + ")");
        return petFoodingControlDatabase.getAutoLoginDao().insert(autoLogin)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Long> save(Photo photo) {
        Log.d(TAG, "save(" + photo + ")");
        return petFoodingControlDatabase.getPhotoDao().insert(photo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable update(Photo photo) {
        Log.d(TAG, "update(" + photo + ")");
        return petFoodingControlDatabase.getPhotoDao().update(photo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Long> save(Pet pet) {
        Log.d(TAG, "save(" + pet + ")");
        return petFoodingControlDatabase.getPetDao().insert(pet)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable update(Pet pet) {
        Log.d(TAG, "update(" + pet + ")");
        return petFoodingControlDatabase.getPetDao().update(pet)
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
    public void setUserLogged(User user) {
        Log.d(TAG, "setUserLogged(" + user + ")");
        userLogged.setValue(user);
    }

    @Override
    public MutableLiveData<User> getUserLogged() {
        return userLogged;
    }

    @Override
    public LiveData<List<Pet>> getUserPets() {
        return userPets;
    }

    @Override
    public void setUserPets(LiveData<List<Pet>> userPets) {
        this.userPets = userPets;
    }

    @Override
    public Flowable<List<Pet>> getAllUserPetsByUserId(Long userId) {
        Log.d(TAG, "getAllUserPetsByUserId(" + userId + ")");
        return petFoodingControlDatabase.getPetDao().getAllUserPetsByUserId(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
