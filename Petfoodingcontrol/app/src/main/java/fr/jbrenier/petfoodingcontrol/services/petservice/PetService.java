package fr.jbrenier.petfoodingcontrol.services.petservice;

import androidx.lifecycle.LiveData;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.androidextras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.PetFeeders;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.food.Fooding;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import fr.jbrenier.petfoodingcontrol.domain.model.Feeder;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;

public interface PetService {
    SingleLiveEvent<Pet> save(Object object, Pet pet);
    SingleLiveEvent<Integer> update(Object object, Pet pet);
    LiveData<List<Pet>> getUserPets(User user);
    SingleLiveEvent<Feeder> checkFeederExistance(Object object, String email);
    SingleLiveEvent<Integer> savePetFeeders(Object object, List<PetFeeders> petFeedersList);
    PetFoodingControlRepository getPfcRepository();
    LiveData<List<Fooding>> getPetFoodings(Pet pet);
}
