package fr.jbrenier.petfoodingcontrol.services.userservice;

import android.content.Context;

import java.util.Map;

import fr.jbrenier.petfoodingcontrol.android.application.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.android.extras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;

/**
 * The User Service Contract.
 * @author Jérôme Brenier
 */
public interface UserService {
    void initLogin(Context context);
    SingleLiveEvent<Integer> tryToLog(Context context, String email, String password,
                                      boolean isKeepLogged);
    SingleLiveEvent<User> save(Context context, User user);
    SingleLiveEvent<Integer> update(Context context, User user);
    SingleLiveEvent<Integer> update(Object object, Map<UserServiceKeysEnum, String> userData);
    void clearKeepMeLogged();
    void leave();
    void clearDisposables(Object object);
    PetFoodingControlRepository getPfcRepository();
    PetFoodingControl getPetFoodingControl();
}
