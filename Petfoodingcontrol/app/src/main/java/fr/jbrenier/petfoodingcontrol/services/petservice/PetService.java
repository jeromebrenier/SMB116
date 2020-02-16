package fr.jbrenier.petfoodingcontrol.services.petservice;

import androidx.lifecycle.LiveData;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.android.extras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.PetFeeder;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.food.Fooding;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.weight.Weighing;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import fr.jbrenier.petfoodingcontrol.domain.model.Feeder;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;

public interface PetService {
    SingleLiveEvent<Pet> save(Object object, Pet pet);
    SingleLiveEvent<Boolean> delete(Object object, Pet pet);
    SingleLiveEvent<Integer> update(Object object, Pet pet);
    LiveData<List<Pet>> getUserPets(User user);
    SingleLiveEvent<Feeder> checkFeederExistance(Object object, String email);
    SingleLiveEvent<List<Feeder>> getFeeders(Object object, Pet pet);
    SingleLiveEvent<Integer> savePetFeeders(Object object, List<PetFeeder> petFeederList);
    PetFoodingControlRepository getPfcRepository();
    LiveData<List<Fooding>> getPetFoodings(Pet pet);
    LiveData<List<Fooding>> getDailyPetFoodings(Pet pet);
    SingleLiveEvent<Boolean> savePetFooding(Object object, Fooding fooding);
    LiveData<List<Weighing>> getWeighingsForPet(Pet pet);
    SingleLiveEvent<Boolean> saveNewWeighing(Object object, Weighing weighing);
}
