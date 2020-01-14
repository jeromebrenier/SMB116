package fr.jbrenier.petfoodingcontrol.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fr.jbrenier.petfoodingcontrol.repository.PetRepository;
import fr.jbrenier.petfoodingcontrol.repository.PetRepositoryDaoImpl;
import fr.jbrenier.petfoodingcontrol.repository.UserRepository;
import fr.jbrenier.petfoodingcontrol.repository.UserRepositoryDaoImpl;

@Module
public class RepositoryModule {
    @Provides
    @Singleton
    public UserRepository getUserRepository() {
        return new UserRepositoryDaoImpl();
    }

    @Provides
    @Singleton
    public PetRepository getPetRepository() {
        return new PetRepositoryDaoImpl();
    }
}
