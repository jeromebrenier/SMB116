package fr.jbrenier.petfoodingcontrol.ui.activities.main;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.androidextras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.di.app.AppComponent;
import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.services.petservice.PetService;
import fr.jbrenier.petfoodingcontrol.services.photoservice.PhotoService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserServiceKeysEnum;

public class MainActivityViewModel extends ViewModel {
    /** LOGGING */
    private static final String TAG = "MainActivityViewModel";

    @Inject
    UserService userService;

    @Inject
    PhotoService photoService;

    @Inject
    PetService petService;

    private MutableLiveData<User> userLogged;
    private MutableLiveData<Bitmap> userPhoto;
    private LiveData<List<Pet>> userPets;

    MainActivityViewModel(AppComponent appComponent) {
        appComponent.inject(this);
        userLogged = userService.getPfcRepository().getUserLogged();
        userPets = petService.getPfcRepository().getUserPets();
    }

    /**
     * Update the user's pets and photo according to the user passed in parameter.
     * @param user the new user
     */
    void updateUserPetsAndPhoto(User user) {
        if (user != null) {
            petService.setUserPets(user);
            userPhoto = photoService.get(this, user);
            Log.i(TAG, "User logged changed to " + user.getUserId().toString());
        } else {
            Log.i(TAG, "User logged changed to null");
        }
    }

    SingleLiveEvent<Integer> updateUser(Map<UserServiceKeysEnum, String> userData) {
        return userService.update(this, userData);
    }

    /**
     * Invoke the userService logout.
     */
    void logout() {
        userService.logout();
    }

    /**
     * Clear the disposables linked.
     */
    void clear() {
        userService.clearDisposables(this);
        photoService.clearDisposables(this);
    }

    void leave() {
        userService.leave();
    }

    public MutableLiveData<User> getUserLogged() {
        return userLogged;
    }

    public MutableLiveData<Bitmap> getUserPhoto() {
        return userPhoto;
    }

    public LiveData<List<Pet>> getUserPets() {
        return userPets;
    }
}
