package fr.jbrenier.petfoodingcontrol.services.petservice;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;

public class PetServiceImpl implements PetService {
    /** LOGGING */
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

    /**
     * Populate the user's pets. If the User given in parameter is null, set the userPets value
     * to null.
     * Return a SingleLiveEvent<Integer> with a value 0 if the operation is a success
     * and 0 otherwise.
     * @param user the user logged
     */
    @Override
    public void setUserPets(User user) {
        pfcRepository.setUserPets(LiveDataReactiveStreams
                .fromPublisher(pfcRepository.getAllUserPetsByUserId(user.getUserId())));
    }

    @Override
    public PetFoodingControlRepository getPfcRepository() {
        return pfcRepository;
    }
}
