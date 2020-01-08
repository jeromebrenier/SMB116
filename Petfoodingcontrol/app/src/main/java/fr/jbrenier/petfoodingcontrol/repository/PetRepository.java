package fr.jbrenier.petfoodingcontrol.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;

/**
 * Data repository for Pet
 * @author Jérôme Brenier
 */
public class PetRepository {

    private final MutableLiveData<List<Pet>> userPets = new MutableLiveData<>();

    @Inject
    public PetRepository() {
    }

    public Pet getById(String id) {
        return null;
    }

    public MutableLiveData<List<Pet>> getUserPets() {
        return userPets;
    }

}
