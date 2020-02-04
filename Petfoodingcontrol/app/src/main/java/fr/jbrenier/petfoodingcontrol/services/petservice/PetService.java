package fr.jbrenier.petfoodingcontrol.services.petservice;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.androidextras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.entities.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;

public interface PetService {
    SingleLiveEvent<Pet> save(Context context, Pet pet);
    SingleLiveEvent<Integer> update(Context context, Pet pet);
    LiveData<List<Pet>> getUserPets(User user);
    PetFoodingControlRepository getPfcRepository();
}
