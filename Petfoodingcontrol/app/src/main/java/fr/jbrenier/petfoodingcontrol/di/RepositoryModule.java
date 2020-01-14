package fr.jbrenier.petfoodingcontrol.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepositoryDaoImpl;

@Module
public class RepositoryModule {
    @Provides
    @Singleton
    public PetFoodingControlRepository getPetFoodingControlRepository() {
        return new PetFoodingControlRepositoryDaoImpl();
    }
}
