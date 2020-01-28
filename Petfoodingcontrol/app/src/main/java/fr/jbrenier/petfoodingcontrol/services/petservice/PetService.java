package fr.jbrenier.petfoodingcontrol.services.petservice;

import android.content.Context;

import fr.jbrenier.petfoodingcontrol.androidextras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;

public interface PetService {
    SingleLiveEvent<Pet> save(Context context, Pet pet);
    SingleLiveEvent<Integer> update(Context context, Pet pet);
    void setUserPets(User user);
    PetFoodingControlRepository getPfcRepository();
}
