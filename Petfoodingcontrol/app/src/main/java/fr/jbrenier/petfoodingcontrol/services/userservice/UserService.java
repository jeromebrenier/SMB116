package fr.jbrenier.petfoodingcontrol.services.userservice;

import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;

public interface UserService {
    void initLogin();
    MutableLiveData<Integer> tryToLog(String email, String password, boolean isKeepLogged);
    MutableLiveData<Integer> save(User user);
    MutableLiveData<Integer> update(User user);
    void logout();
    void leave();
    PetFoodingControlRepository getPfcRepository();
    SharedPreferences getSharedPreferences();
}
