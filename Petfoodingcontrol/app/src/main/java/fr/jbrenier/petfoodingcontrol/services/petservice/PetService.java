package fr.jbrenier.petfoodingcontrol.services.petservice;

import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;

public interface PetService {
    void setUserPets(User user);
    PetFoodingControlRepository getPfcRepository();
}
