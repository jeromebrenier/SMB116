package fr.jbrenier.petfoodingcontrol.ui.fragments.main.pets;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;

public class PetFragmentViewModel extends ViewModel {

    private List<Pet> userPets = new ArrayList<>();

    public void refresh(List<Pet> newList) {
        userPets.clear();
        if (newList != null) {
            userPets.addAll(newList);
        }
    }

    public List<Pet> getUserPets() {
        return userPets;
    }
}
