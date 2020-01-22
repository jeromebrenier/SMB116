package fr.jbrenier.petfoodingcontrol.services.petservice;

import android.content.SharedPreferences;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;

public class PetServiceImpl implements PetService {
    /**
     * LOGGING
     */
    private static final String TAG = "PetService";

    private PetFoodingControlRepository pfcRepository;
    private SharedPreferences sharedPreferences;
    private UserService userService;

    @Inject
    public PetServiceImpl(PetFoodingControlRepository pfcRepository, SharedPreferences
            sharedPreferences, UserService userService) {
        this.pfcRepository = pfcRepository;
        this.sharedPreferences = sharedPreferences;
        this.userService = userService;
    }

    public PetFoodingControlRepository getPfcRepository() {
        return pfcRepository;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
