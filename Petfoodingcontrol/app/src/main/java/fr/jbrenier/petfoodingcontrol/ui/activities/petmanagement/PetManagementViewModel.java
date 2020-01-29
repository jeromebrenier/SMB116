package fr.jbrenier.petfoodingcontrol.ui.activities.petmanagement;

import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.pet.food.FoodSettings;
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.user.User;

public class PetManagementViewModel extends ViewModel {
    private Pet petToAdd;
    private Photo petPhoto;
    private List<User> petFeedersList;
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

    public List<User> getPetFeedersList() {
        return petFeedersList;
    }

    public void setPetFeedersList(List<User> petFeedersList) {
        this.petFeedersList = petFeedersList;
    }

    public FoodSettings getFoodSettings() {
        return foodSettings;
    }

    public void setFoodSettings(FoodSettings foodSettings) {
        this.foodSettings = foodSettings;
    }
}
