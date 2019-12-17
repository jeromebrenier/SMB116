package fr.jbrenier.petfoodingcontrol.domain.user;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;

/**
 * A Pet Fooding Control user.
 * @author Jérôme Brenier
 */
public class User {
    private String id;
    private String displayedName;
    private String email;
    private String password;
    private Photo photo;
    private List<Pet> petOwned;
    private List<Pet> petAuthorizedToFed;
}
