package fr.jbrenier.petfoodingcontrol.ui.activities.petmanagement;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.androidextras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.food.FoodSettings;
import fr.jbrenier.petfoodingcontrol.domain.entities.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import fr.jbrenier.petfoodingcontrol.domain.model.Feeder;
import fr.jbrenier.petfoodingcontrol.services.petservice.PetService;
import fr.jbrenier.petfoodingcontrol.services.photoservice.PhotoService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;

public class PetManagementViewModel extends ViewModel {

    /* LOGGING */
    private static final String TAG = "PetManagementViewModel";

    @Inject
    UserService userService;

    @Inject
    PhotoService photoService;

    @Inject
    PetService petService;

    private Pet petToAdd;
    private Photo petPhoto;
    private LiveData<List<Feeder>> petFeeders;
    private SingleLiveEvent<Boolean> petSavingStatus = new SingleLiveEvent<>();
    /* Needed for the feeders list fragment */
    private List<Feeder> petFeedersArrayList = new ArrayList<>();
    private FoodSettings foodSettings;

    /* REFERENCES FOR CLEARING */
    private SingleLiveEvent<Pet> saveNewPet;
    private Observer<Pet> saveNewPetObserver;
    private SingleLiveEvent<Boolean> saveNewPetPhoto;
    private Observer<Boolean> saveNewPetPhotoObserver;

    /**
     * Save in the DB a new pet present in the viewModel.
     */
    private SingleLiveEvent<Pet> saveNewPet() {
        if (petToAdd != null) {
            addFoodSettingsToPet();
            return petService.save(this, petToAdd);
        }
        return null;
    }

    /**
     * Add the food settings to the Pet present in the viewModel
     */
    private void addFoodSettingsToPet() {
        if (foodSettings != null) {
            petToAdd.setFoodSettings(foodSettings);
        }
    }

    /**
     * Save the pet data in the DB.
     */
    SingleLiveEvent<Boolean> savePetData() {
        saveNewPet = saveNewPet();
        saveNewPetObserver = pet -> {
            if (pet != null) {
                Log.i(TAG, "Pet saved.");
                saveNewPetPhoto = savePetPhoto(pet);
                saveNewPetPhotoObserver = result -> {
                    if (result) {
                        Log.i(TAG, "Pet photo saved.");
                    } else {
                        Log.i(TAG, "Pet photo updated.");
                    }
                };
                saveNewPetPhoto.observeForever(saveNewPetPhotoObserver);
                savePetFeeders(pet);
                petSavingStatus.setValue(true);
            } else {
                petSavingStatus.setValue(false);
            }
        };
        saveNewPet.observeForever(saveNewPetObserver);
        return petSavingStatus;
    }

    /**
     * Save in the DB a new pet present in the viewModel.
     * @param pet Pet the photo belongs to
     */
    private SingleLiveEvent<Boolean> savePetPhoto(Pet pet) {
        if (petPhoto != null) {
            if (petPhoto.getPhotoId() == null) {
                return photoService.save(this, petPhoto, pet);
            } else {
                return photoService.update(this, petPhoto);
            }
        }
        return null;
    }

    private SingleLiveEvent<Boolean> savePetFeeders(Pet pet) {
        SingleLiveEvent<Boolean> result = new SingleLiveEvent<>();
        result.setValue(true);
        return result;
    }

    public SingleLiveEvent<Feeder> checkFeederExistance(String email) {
        return petService.checkFeederExistance(this, email);
    }

    @Override
    public void onCleared() {
        saveNewPet.removeObserver(saveNewPetObserver);
        saveNewPetPhoto.removeObserver(saveNewPetPhotoObserver);
    }

    public Pet getPetToAdd() {
        return petToAdd;
    }

    public void setPetToAdd(Pet petToAdd) {
        this.petToAdd = petToAdd;
    }

    public Photo getPetPhoto() {
        return petPhoto;
    }

    public void setPetPhoto(Photo petPhoto) {
        this.petPhoto = petPhoto;
    }

    public LiveData<List<Feeder>> getPetFeeders() {
        return petFeeders;
    }

    public List<Feeder> getPetFeedersArrayList() {
        return petFeedersArrayList;
    }

    public FoodSettings getFoodSettings() {
        return foodSettings;
    }

    public void setFoodSettings(FoodSettings foodSettings) {
        this.foodSettings = foodSettings;
    }
}
