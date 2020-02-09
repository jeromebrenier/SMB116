package fr.jbrenier.petfoodingcontrol.ui.activities.petfooding;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;

public class PetFoodingViewModel extends ViewModel {
    private MutableLiveData<Pet> pet = new MutableLiveData<>();

    public PetFoodingViewModel(Pet pet) {
        this.pet.setValue(pet);
    }

    public MutableLiveData<Pet> getPet() {
        return pet;
    }
}
