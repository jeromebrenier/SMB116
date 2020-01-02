package fr.jbrenier.petfoodingcontrol.db;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;

/**
 * DAO interface for Pet's data access.
 * @author Jérôme Brenier
 */
public interface DAOPet {
    Pet getById(String id);
}
