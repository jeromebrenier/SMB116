package fr.jbrenier.petfoodingcontrol.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import java.util.ArrayList;
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

public class PetFoodingControlRepositoryImpl implements PetFoodingControlRepository {

    private static final String DB_NAME = "pfc_db";
    private final PetFoodingControlDatabase petFoodingControlDatabase;
    private final MutableLiveData<User> userLogged = new MutableLiveData<>();
    private LiveData<List<Pet>> userPets;

    @Inject
    public PetFoodingControlRepositoryImpl(Application application) {
        petFoodingControlDatabase = Room.databaseBuilder(application, PetFoodingControlDatabase.class, DB_NAME).build();
    }

    @Override
    public Single<User> getUserByEmail(String email) {
        return petFoodingControlDatabase.getUserDao().getUserByEmail(email)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<User> getUserById(Long userId) {
        return petFoodingControlDatabase.getUserDao().getUserById(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Photo> getUserPhoto(User user) {
        return petFoodingControlDatabase.getPhotoDao().getPhotoById(user.getPhotoId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Long> save(User user) {
        return petFoodingControlDatabase.getUserDao().insert(user)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable update(User user) {
        return petFoodingControlDatabase.getUserDao().update(user)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<User> getUserByAutoLogin(String autoLoginToken) {
        return petFoodingControlDatabase.getAutoLoginDao().getUserByAutoLogin(autoLoginToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable insert(AutoLogin autoLogin) {
        return petFoodingControlDatabase.getAutoLoginDao().insert(autoLogin)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Long> save(Photo photo) {
        return petFoodingControlDatabase.getPhotoDao().insert(photo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable update(Photo photo) {
        return petFoodingControlDatabase.getPhotoDao().update(photo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Pet> getPetById(Long petId) {
        return petFoodingControlDatabase.getPetDao().getPetbyId(petId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Photo> getPetPhoto(Pet pet) {
        return petFoodingControlDatabase.getPhotoDao().getPhotoById(pet.getPhotoId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void setUserLogged(User user) {
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
        return petFoodingControlDatabase.getPetDao().getAllUserPetsByUserId(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
