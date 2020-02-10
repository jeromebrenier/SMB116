package fr.jbrenier.petfoodingcontrol.ui.activities.petfooding;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.services.petservice.PetService;

public class PetFoodingViewModel extends ViewModel {


    @Inject
    PetService petService;

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
