package fr.jbrenier.petfoodingcontrol.services.userservice;

import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;

public interface UserService {
    void initLogin();
    MutableLiveData<Integer> tryToLog(String email, String password, boolean isKeepLogged);
    PetFoodingControlRepository getPfcRepository();
    SharedPreferences getSharedPreferences();
}
