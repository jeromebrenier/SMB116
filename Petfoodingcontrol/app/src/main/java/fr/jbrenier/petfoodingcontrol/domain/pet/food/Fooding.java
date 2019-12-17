package fr.jbrenier.petfoodingcontrol.domain.pet.food;

import java.util.Date;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.user.User;

/**
 * A fooding action.
 * @author Jérôme Brenier
 */
public class Fooding {
    private User user;
    private Pet pet;
    private Date date;
    private int quantity;
}
