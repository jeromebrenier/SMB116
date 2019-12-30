package fr.jbrenier.petfoodingcontrol.ui.activities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.user.User;

public class MainActivityViewModel extends ViewModel {
    private final MutableLiveData<User> userLogged = new MutableLiveData<User>();
    private final MutableLiveData<List<Pet>> userPets = new MutableLiveData<List<Pet>>();

    public void setUserLogged(User user) {
        userLogged.setValue(user);
    }

    public LiveData<User> getUserLogged() {
        return userLogged;
    }

    public MutableLiveData<List<Pet>> getUserPets() {
        return userPets;
    }
}
