package fr.jbrenier.petfoodingcontrol.db;

import javax.inject.Inject;
import javax.inject.Singleton;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;

/**
 * Class for getting data about Pets.
 * @author Jérôme Brenier
 */
@Singleton
public class DAOPetImpl implements DAOPet {

    @Inject
    public DAOPetImpl() {
    }

    public Pet getById(String id) {
        return null;
    }
}
