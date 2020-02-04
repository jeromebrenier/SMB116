package fr.jbrenier.petfoodingcontrol.services.userservice;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import fr.jbrenier.petfoodingcontrol.androidextras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.entities.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;

public interface UserService {
    void initLogin(Context context);
    SingleLiveEvent<Integer> tryToLog(Context context, String email, String password,
                                      boolean isKeepLogged);
    SingleLiveEvent<User> save(Context context, User user);
    SingleLiveEvent<Integer> update(Context context, User user);
    SingleLiveEvent<Integer> update(Object object, Map<UserServiceKeysEnum, String> userData);
    void logout();
    void leave();
    void clearDisposables(Object object);
    PetFoodingControlRepository getPfcRepository();
    SharedPreferences getSharedPreferences();
}
