package fr.jbrenier.petfoodingcontrol.ui.activities.petfooding;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;

public class PetFoodingViewModel extends ViewModel {

    /** Logging */
    private static final String TAG = "PetFoodingViewModel";

    private MutableLiveData<Pet> pet = new MutableLiveData<>();

    public PetFoodingViewModel(Pet pet) {
        Log.d(TAG, "Pet managed : " + pet.getPetId());
        this.pet.setValue(pet);
    }

    public MutableLiveData<Pet> getPet() {
        return pet;
    }
}
