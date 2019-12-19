package fr.jbrenier.petfoodingcontrol.ui.activities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import fr.jbrenier.petfoodingcontrol.domain.user.User;

public class LoginActivityViewModel extends ViewModel {
    private final MutableLiveData<User> userLogged = new MutableLiveData<User>();

    public void setUserLogged(User user) {
        userLogged.setValue(user);
    }

    public LiveData<User> getUserLogged() {
        return userLogged;
    }

}
