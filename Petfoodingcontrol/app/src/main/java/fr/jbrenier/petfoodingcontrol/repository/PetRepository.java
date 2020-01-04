package fr.jbrenier.petfoodingcontrol.repository;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;

/**
 * Data repository for Pet
 * @author Jérôme Brenier
 */
public class PetRepository {

    @Inject
    public PetRepository() {
    }

    public Pet getById(String id) {
        return null;
    }
}
