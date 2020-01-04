package fr.jbrenier.petfoodingcontrol;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.repository.DaggerRepositoryComponent;
import fr.jbrenier.petfoodingcontrol.repository.RepositoryComponent;

public class PetFoodingControl extends Application {

    private final MutableLiveData<User> userLogged = new MutableLiveData<User>();

    private RepositoryComponent repositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        repositoryComponent = DaggerRepositoryComponent.builder().build();
    }

    public void setUserLogged(User user) {
        userLogged.setValue(user);
    }

    public MutableLiveData<User> getUserLogged() {
        return userLogged;
    }

    public RepositoryComponent getRepositoryComponent() {
        return repositoryComponent;
    }
}
