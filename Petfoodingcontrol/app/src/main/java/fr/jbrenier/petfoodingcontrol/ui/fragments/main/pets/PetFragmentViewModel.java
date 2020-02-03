package fr.jbrenier.petfoodingcontrol.ui.fragments.main.pets;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.di.app.AppComponent;
import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.services.petservice.PetService;
import fr.jbrenier.petfoodingcontrol.services.photoservice.PhotoService;

public class PetFragmentViewModel extends ViewModel {
    private static final String TAG = "PetFragmentViewModel";

    @Inject
    PetService petService;

    @Inject
    PhotoService photoService;

    private List<Pet> userPets = new ArrayList<>();

    public PetFragmentViewModel(AppComponent appComponent) {
        appComponent.inject(this);
        if (petService.getPfcRepository().getUserLogged() != null) {
            petService.getPfcRepository().getUserLogged().observe(this, user -> {
                if (user == null) {
                    observeUserPets(false);
                } else {
                    refresh(petService.getPfcRepository().getUserPets().getValue());
                    adapter.notifyDataSetChanged();
                    observeUserPets(true);
                }
            });
        }
    }

    public void refresh(List<Pet> newList) {
        userPets.clear();
        if (newList != null) {
            userPets.addAll(newList);
        }
    }

    private void manageUserAndPets() {
        if (petService.getPfcRepository().getUserLogged().getValue() != null) {
            adapter.setUserLogged(petService.getPfcRepository().getUserLogged().getValue());
        }
        if (petService.getPfcRepository().getUserPets() != null &&
                petService.getPfcRepository().getUserPets().getValue() != null ) {
            petFragmentViewModel.refresh(petService.getPfcRepository().getUserPets().getValue());
            adapter.notifyDataSetChanged();
            userPetObserver = list -> {
                Log.i(TAG, "Pet list has changed.");
                petFragmentViewModel.refresh(list);
                adapter.notifyDataSetChanged();
            };
            petService.getPfcRepository().getUserPets().observe(getViewLifecycleOwner(),
                    userPetObserver);
        }
    }

    public List<Pet> getUserPets() {
        return userPets;
    }

    User getUserLogged() {
        return petService.getPfcRepository().getUserLogged().getValue();
    }
}
