package fr.jbrenier.petfoodingcontrol.android.activities.main;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.android.application.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.android.extras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.PetFeeder;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import fr.jbrenier.petfoodingcontrol.services.disposablemanagement.DisposableManager;
import fr.jbrenier.petfoodingcontrol.services.disposablemanagement.DisposableOwner;
import fr.jbrenier.petfoodingcontrol.services.petservice.PetService;
import fr.jbrenier.petfoodingcontrol.services.photoservice.PhotoService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserServiceKeysEnum;

/**
 * The main activity view model.
 * @author Jérôme Brenier
 */
public class MainActivityViewModel extends ViewModel implements DisposableOwner {
    /** Logging */
    private static final String TAG = "MainActivityViewModel";

    @Inject
    DisposableManager disposableManager;

    @Inject
    UserService userService;

    @Inject
    PhotoService photoService;

    @Inject
    PetService petService;

    private PetFoodingControl petFoodingControl;
    private MutableLiveData<Bitmap> userPhoto = new MutableLiveData<>();
    private Observer<Bitmap> userPhotoObserver = userPhoto::setValue;
    private Map<SingleLiveEvent<Bitmap>, Observer<Bitmap>> userPhotoListenerMap = new HashMap<>();
    private LiveData<List<Pet>> userPets;
    /** Needed for the pet list fragment */
    private List<Pet> userPetsArrayList = new ArrayList<>();
    private MutableLiveData<Boolean> userPetsArrayListChanged = new MutableLiveData<>(false);

    private Map<LiveData<List<Pet>>, Observer<List<Pet>>> userPetsListenerMap = new HashMap<>();

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
            userPhotoListenerMap.forEach(SingleLiveEvent::removeObserver);
            SingleLiveEvent<Bitmap> userPhotoFromDb = photoService.get(this, user);
            userPhotoFromDb.observeForever(userPhotoObserver);
            userPhotoListenerMap.put(userPhotoFromDb, userPhotoObserver);
            Log.i(TAG, "User logged changed to " + user.getUserId().toString());
        } else {
            Log.i(TAG, "User logged changed to null");
        }
    }

    /**
     * Refresh the user's photo after update.
     */
    void refreshPhoto(User user) {
        User userToUse = user;
        if (userToUse == null && petFoodingControl.getUserLogged() != null) {
            userToUse = petFoodingControl.getUserLogged().getValue();
        }
        if (userToUse != null) {
            userPhotoListenerMap.forEach(SingleLiveEvent::removeObserver);
            SingleLiveEvent<Bitmap> userPhotoFromDb = photoService.get(this, userToUse);
            userPhotoFromDb.observeForever(userPhotoObserver);
            userPhotoListenerMap.put(userPhotoFromDb, userPhotoObserver);
            Log.d(TAG, "User's photo updated");
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

    /**
     * Update the user with the data given in parameter.
     * @param userData the updated data
     * @return 0 if update process successful, 1 otherwise
     */
    SingleLiveEvent<Integer> updateUser(Map<UserServiceKeysEnum, String> userData) {
        return userService.update(this, userData);
    }

    /**
     * Update the user'photo with the data given in parameter.
     * @param userData the updated data
     * @return 0 if update process successful, 1 otherwise
     */
    SingleLiveEvent<Integer> updateUserPhoto(Map<UserServiceKeysEnum, String> userData) {
        return photoService.update(this, petFoodingControl.getUserLogged().getValue(), userData);
    }

    /**
     * Remove the current user logged as a feeder for the pet given in parameter.
     * @param pet the pet for which the user logged has to be removed as a feeder
     * @return true if removal process successful, false otherwise
     */
    SingleLiveEvent<Boolean> removeFeederForPet(Pet pet) {
        if (petFoodingControl.getUserLogged().getValue() != null) {
            PetFeeder petFeederToRemove = new PetFeeder(pet.getPetId(),
                    petFoodingControl.getUserLogged().getValue().getUserId());
            return petService.removePetFeeder(this, petFeederToRemove);
        } else {
            Log.e(TAG, "User logged null, cannot continue");
            SingleLiveEvent<Boolean> failure = new SingleLiveEvent<>();
            failure.setValue(false);
            return failure;
        }
    }

    /**
     * Delete the pet given in parameter.
     * @param pet the pet to delete
     * @return true if delete process successful, false otherwise
     */
    SingleLiveEvent<Boolean> deletePet(Pet pet) {
        return petService.delete(this, pet);
    }

    /**
     * Invoke the userService logout.
     */
    void logout() {
        userService.leave();
        userService.clearKeepMeLogged();
        clearDisposables();
        userPetsListenerMap.forEach(LiveData::removeObserver);
    }

    /**
     * Invoked when Main Activity is finishing to clear the disposables and log out.
     */
    void finish() {
        clearDisposables();
        userService.leave();
    }

    public MutableLiveData<Bitmap> getPetPhoto(Pet pet) {
        return photoService.getPetBitmap(this, pet);
    }

    public LiveData<Integer> getPetStatus(Pet pet) {
        return petService.getPetStatus(pet);
    }

    public MutableLiveData<Bitmap> getUserPhoto() {
        return userPhoto;
    }

    LiveData<List<Pet>> getUserPets() {
        return userPets;
    }

    public List<Pet> getUserPetsArrayList() {
        return userPetsArrayList;
    }

    public MutableLiveData<Boolean> getUserPetsArrayListChanged() {
        return userPetsArrayListChanged;
    }

    Map<LiveData<List<Pet>>, Observer<List<Pet>>> getUserPetsListenerMap() {
        return userPetsListenerMap;
    }

    @Override
    public void clearDisposables() {
        disposableManager.clear(this);
    }
}
