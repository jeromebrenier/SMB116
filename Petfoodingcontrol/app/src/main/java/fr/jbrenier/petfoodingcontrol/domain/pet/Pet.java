package fr.jbrenier.petfoodingcontrol.domain.pet;

import java.util.Date;

import fr.jbrenier.petfoodingcontrol.domain.pet.food.FoodSettings;
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;

/**
 * A Pet that can be fed and weight through the application
 * Pet Fooding Control.
 * @author Jérôme Brenier
 */
public class Pet {
    private String id;
    private String name;
    private Photo photo;
    private Date birthDate;
    private FoodSettings foodSettings;
}
