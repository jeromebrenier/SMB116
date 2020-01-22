package fr.jbrenier.petfoodingcontrol.services.petservice;

import android.content.SharedPreferences;

import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;

public interface PetService {
    PetFoodingControlRepository getPfcRepository();
    SharedPreferences getSharedPreferences();
}
