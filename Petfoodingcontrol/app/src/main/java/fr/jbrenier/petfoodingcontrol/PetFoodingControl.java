package fr.jbrenier.petfoodingcontrol;

import android.app.Application;

import fr.jbrenier.petfoodingcontrol.di.DaggerRepositoryComponent;
import fr.jbrenier.petfoodingcontrol.di.RepositoryComponent;

/**
 * The Pet Fooding Control application class.
 * @author Jérôme Brenier
 */
public class PetFoodingControl extends Application {

    private RepositoryComponent repositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        repositoryComponent = DaggerRepositoryComponent.builder().build();
    }

    public RepositoryComponent getRepositoryComponent() {
        return repositoryComponent;
    }
}
