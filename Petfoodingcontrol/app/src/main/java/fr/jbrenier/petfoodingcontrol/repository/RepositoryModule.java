package fr.jbrenier.petfoodingcontrol.repository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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
