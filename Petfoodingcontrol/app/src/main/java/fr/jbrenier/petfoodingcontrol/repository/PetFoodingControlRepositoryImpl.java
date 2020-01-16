package fr.jbrenier.petfoodingcontrol.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import java.util.List;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.db.PetFoodingControlDatabase;
import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import io.reactivex.Completable;
import io.reactivex.Single;

public class PetFoodingControlRepositoryImpl implements PetFoodingControlRepository {

    private static final String DB_NAME = "pfc_db";
    private final PetFoodingControlDatabase petFoodingControlDatabase;
    private final MutableLiveData<User> userLogged = new MutableLiveData<>();
    private final MutableLiveData<Pet> userPets = new MutableLiveData<>();

    @Inject
    public PetFoodingControlRepositoryImpl(Application application) {
        petFoodingControlDatabase = Room.databaseBuilder(application, PetFoodingControlDatabase.class, DB_NAME).build();
    }

    @Override
    public User getUserByCredentials(String email, String password) {
/*        User user = petFoodingControlDatabase.getUserDao().getUserbyEmail(email);
        if (verifyUserPassword(password, user.getPassword())) {
            return user;
        }*/
        return null;
    }

    @Override
    public boolean checkUserExistance(User user) {
        final boolean[] result = new boolean[1];
        Thread getThread = new Thread(() -> result[0] =
                petFoodingControlDatabase.getUserDao().getUserbyId(user.getUserId()) != null);
        getThread.start();
        System.out.println("Result ------> " + result[0]);
        return result[0];
    }

    @Override
    public Single<Photo> getUserPhoto(User user) {
        return petFoodingControlDatabase.getPhotoDao().getPhotoById(user.getPhotoId());
    }

    @Override
    public String getUserPasswd(String email) {
        return "test";
    }

    @Override
    public boolean verifyUserPassword(String passwordToCheck, String storedPassword) {
        return true;
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
    public Completable save(User user) {
        return petFoodingControlDatabase.getUserDao().insert(user);
    }

    @Override
    public void save(Photo photo) {
        Thread insertThread =
                new Thread(() -> petFoodingControlDatabase.getPhotoDao().insert(photo));
        insertThread.start();
    }

    @Override
    public Pet getPetById(String id) {
        return null;
    }

    @Override
    public void setUserPets(User user) {
        userPets.setValue(null);
    }

    @Override
    public Photo getPetPhoto(Pet pet) {
        return new Photo(1000L, "phooooo");
    }

    @Override
    public MutableLiveData<List<Pet>> getUserPets() {
        if (userPets.getValue() == null) {
            //userPets.setValue();
        }
        return userLogged == null ? null : new MutableLiveData<>();
    }
}
