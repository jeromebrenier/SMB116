package fr.jbrenier.petfoodingcontrol.ui.activities.petmanagement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import fr.jbrenier.petfoodingcontrol.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.entities.pet.food.FoodSettings;
import fr.jbrenier.petfoodingcontrol.entities.photo.Photo;
import fr.jbrenier.petfoodingcontrol.entities.user.User;

public class PetManagementViewModel extends ViewModel {
    private Pet petToAdd;
    private Photo petPhoto;
    private LiveData<List<User>> petFeeders;
    /** Needed for the feeders list fragment */
    private List<User> petFeedersArrayList = new ArrayList<>();
    private FoodSettings foodSettings;

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

    public LiveData<List<User>> getPetFeeders() {
        return petFeeders;
    }

    public List<User> getPetFeedersArrayList() {
        return petFeedersArrayList;
    }

    public FoodSettings getFoodSettings() {
        return foodSettings;
    }

    public void setFoodSettings(FoodSettings foodSettings) {
        this.foodSettings = foodSettings;
    }
}
