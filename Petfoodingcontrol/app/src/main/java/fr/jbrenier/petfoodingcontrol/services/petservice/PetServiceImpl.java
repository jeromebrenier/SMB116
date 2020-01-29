package fr.jbrenier.petfoodingcontrol.services.petservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import java.util.List;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.androidextras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.services.PetFoodingControlService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;
import io.reactivex.disposables.Disposable;

public class PetServiceImpl extends PetFoodingControlService implements PetService {
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

    @Override
    public SingleLiveEvent<Pet> save(Context context, Pet pet) {
        SingleLiveEvent<Pet> savePetResult = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.save(pet).subscribe(
                (petId) -> {
                    pet.setUserId(petId);
                    savePetResult.setValue(pet);
                    Log.i(TAG, "Pet saved with id : " + petId);
                },
                throwable -> {
                    savePetResult.setValue(null);
                    Log.e(TAG, "Pet saving failure", throwable);
                });
        addToCompositeDisposable(context, disposable);
        return savePetResult;
    }

    @Override
    public SingleLiveEvent<Integer> update(Context context, Pet pet) {
        return null;
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
        pfcRepository.setUserPets(LiveDataReactiveStreams.fromPublisher(
                pfcRepository.getAllUserPetsByUserId(user.getUserId())));
    }

    @Override
    public PetFoodingControlRepository getPfcRepository() {
        return pfcRepository;
    }
}
