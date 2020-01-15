package fr.jbrenier.petfoodingcontrol;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import fr.jbrenier.petfoodingcontrol.di.DaggerRepositoryComponent;
import fr.jbrenier.petfoodingcontrol.di.RepositoryComponent;

/**
 * The Pet Fooding Control application class.
 * @author Jérôme Brenier
 */
public class PetFoodingControl extends Application {

    private RepositoryComponent repositoryComponent;
    public MutableLiveData<Boolean> isCameraPermissionGranted = new MutableLiveData<>(false);

    @Override
    public void onCreate() {
        super.onCreate();
        repositoryComponent = DaggerRepositoryComponent.builder().application(this).build();
    }

    public RepositoryComponent getRepositoryComponent() {
        return repositoryComponent;
    }

}
