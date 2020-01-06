package fr.jbrenier.petfoodingcontrol.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fr.jbrenier.petfoodingcontrol.repository.PetRepository;
import fr.jbrenier.petfoodingcontrol.repository.UserRepository;

@Module
public class RepositoryModule {
    @Provides
    @Singleton
    public UserRepository getUserRepository() {
        return new UserRepository();
    }

    @Provides
    @Singleton
    public PetRepository getPetRepository() {
        return new PetRepository();
    }
}
