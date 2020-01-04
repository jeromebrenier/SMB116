package fr.jbrenier.petfoodingcontrol.ui.activities.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;

public class MainActivityViewModel extends ViewModel {
    private final MutableLiveData<List<Pet>> userPets = new MutableLiveData<List<Pet>>();

    public MutableLiveData<List<Pet>> getUserPets() {
        return userPets;
    }
}
