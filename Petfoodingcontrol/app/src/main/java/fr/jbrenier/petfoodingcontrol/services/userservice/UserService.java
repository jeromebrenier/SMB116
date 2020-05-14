package fr.jbrenier.petfoodingcontrol.services.userservice;

import java.util.Map;

import fr.jbrenier.petfoodingcontrol.android.application.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.android.extras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.services.disposablemanagement.DisposableOwner;

/**
 * The User Service Contract.
 * @author Jérôme Brenier
 */
public interface UserService {
    void initLogin(DisposableOwner disposableOwner);
    SingleLiveEvent<Integer> tryToLog(DisposableOwner disposableOwner,
                                      String email,
                                      String password,
                                      boolean isKeepLogged);
    SingleLiveEvent<User> save(DisposableOwner disposableOwner, User user);
    SingleLiveEvent<Integer> update(DisposableOwner disposableOwner, User user);
    SingleLiveEvent<Integer> update(DisposableOwner disposableOwner,
                                    Map<UserServiceKeysEnum, String> userData);
    void clearKeepMeLogged();
    void leave();
    PetFoodingControlRepository getPfcRepository();
    PetFoodingControl getPetFoodingControl();
}
