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
import fr.jbrenier.petfoodingcontrol.services.disposablemanagement.DisposableOwner;

/**
 * The Pet service contract.
 * @author Jérôme Brenier
 */
public interface PetService {
    SingleLiveEvent<Pet> save(DisposableOwner disposableOwner, Pet pet);
    SingleLiveEvent<Boolean> delete(DisposableOwner disposableOwner, Pet pet);
    SingleLiveEvent<Pet> update(DisposableOwner disposableOwner, Pet pet);
    LiveData<List<Pet>> getUserPets(User user);
    SingleLiveEvent<Pet> getPetById(DisposableOwner disposableOwner, Long petId);
    SingleLiveEvent<Feeder> checkFeederExistance(DisposableOwner disposableOwner, String email);
    SingleLiveEvent<List<Feeder>> getFeeders(DisposableOwner disposableOwner, Pet pet);
    SingleLiveEvent<Boolean> removePetFeeder(DisposableOwner disposableOwner, PetFeeder petFeeder);
    SingleLiveEvent<Integer> savePetFeeders(DisposableOwner disposableOwner, List<PetFeeder> petFeederList);
    PetFoodingControlRepository getPfcRepository();
    LiveData<List<Fooding>> getPetFoodings(Pet pet);
    LiveData<Integer> getPetStatus(Pet pet);
    LiveData<List<Fooding>> getDailyPetFoodings(Pet pet);
    SingleLiveEvent<Boolean> savePetFooding(DisposableOwner disposableOwner, Fooding fooding);
    LiveData<List<Weighing>> getWeighingsForPet(Pet pet);
    SingleLiveEvent<Boolean> saveNewWeighing(DisposableOwner disposableOwner, Weighing weighing);
}
