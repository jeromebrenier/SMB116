package fr.jbrenier.petfoodingcontrol.di.repository;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepositoryDaoImpl;

@Module
public class RepositoryModule {

    @Provides
    public PetFoodingControlRepository getPetFoodingControlRepository(Application application) {
        return new PetFoodingControlRepositoryDaoImpl(application);
    }
}
