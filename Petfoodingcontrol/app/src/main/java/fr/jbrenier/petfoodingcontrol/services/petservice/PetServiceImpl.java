package fr.jbrenier.petfoodingcontrol.services.petservice;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveDataReactiveStreams;

import javax.inject.Inject;

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
        pfcRepository.getUserPets().addSource(LiveDataReactiveStreams.fromPublisher(pfcRepository.getPetOwnedbyUserId(user.getUserId())), list -> {
            list.stream().forEach(pet -> {
                if (pfcRepository.getUserPets().getValue().contains())
            });
        });
/*        pfcRepository.getPetOwnedbyUserId(user.getUserId()).subscribe(
                list -> {
                    List<Pet> newList = new ArrayList<>(pfcRepository.getUserPets().getValue());
                    newList.addAll(list);
                    pfcRepository.setUserPets();
                }
        );
        pfcRepository.getPetsforFeeder(user.getUserId()).subscribe(
                list -> {
                    listUserPets.addAll(list);
                }
        );*/
/*        Disposable disposable = pfcRepository.g(user).subscribe(
                (userId) -> {
                    saveUserResult.setValue(0);
                    Log.i(TAG,"User saved with id : " + userId);
                },
                throwable -> {
                    saveUserResult.setValue(1);
                    Log.e(TAG, "User "+ user.getUserId() + " saving failure", throwable);
                });
        addToCompositeDisposable(context, disposable);
        return saveUserResult;*/
    }}
}
