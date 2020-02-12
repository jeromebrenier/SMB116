package fr.jbrenier.petfoodingcontrol.android.activities.main;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.android.application.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.android.extras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
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

    private PetFoodingControl petFoodingControl;
    private MutableLiveData<Bitmap> userPhoto;
    private LiveData<List<Pet>> userPets;
    /** Needed for the pet list fragment */
    private List<Pet> userPetsArrayList = new ArrayList<>();
    private MutableLiveData<Boolean> userPetsArrayListChanged = new MutableLiveData<>(false);

    MainActivityViewModel(PetFoodingControl petFoodingControl) {
        this.petFoodingControl = petFoodingControl;
        petFoodingControl.getAppComponent().inject(this);
    }

    /**
     * Update the user's pets and photo according to the user passed in parameter.
     * @param user the new user
     */
    void updateUserPetsAndPhoto(User user) {
        if (user != null) {
            userPets = petService.getUserPets(user);
            userPhoto = photoService.get(this, user);
            Log.i(TAG, "User logged changed to " + user.getUserId().toString());
        } else {
            Log.i(TAG, "User logged changed to null");
        }
    }

    /**
     * Update the UserPetsArrayList with the retrieve userPets list.
     * Change the MutableLiveData<Boolean> in the same time.
     * @param newUserPetsList the new userPets list
     */
    void updateUserPetsArrayList(List<Pet> newUserPetsList) {
        Log.d(TAG, "updateUserPetsArrayList " + newUserPetsList.size());
        userPetsArrayList.clear();
        userPetsArrayList.addAll(newUserPetsList);
        boolean currentValue = userPetsArrayListChanged.getValue() != null &&
                userPetsArrayListChanged.getValue();
        userPetsArrayListChanged.setValue(currentValue);
    }

    SingleLiveEvent<Integer> updateUser(Map<UserServiceKeysEnum, String> userData) {
        return userService.update(this, userData);
    }

    SingleLiveEvent<Integer> updateUserPhoto(Map<UserServiceKeysEnum, String> userData) {
        return photoService.update(this, petFoodingControl.getUserLogged().getValue(), userData);
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

    public MutableLiveData<Bitmap> getPetPhoto(Pet pet) {
        return photoService.get(this, pet);
    }

    public MutableLiveData<Bitmap> getUserPhoto() {
        return userPhoto;
    }

    public LiveData<List<Pet>> getUserPets() {
        return userPets;
    }

    public List<Pet> getUserPetsArrayList() {
        return userPetsArrayList;
    }

    public MutableLiveData<Boolean> getUserPetsArrayListChanged() {
        return userPetsArrayListChanged;
    }
}
