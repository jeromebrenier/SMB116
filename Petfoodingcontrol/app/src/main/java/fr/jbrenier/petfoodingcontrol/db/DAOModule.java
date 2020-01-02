package fr.jbrenier.petfoodingcontrol.db;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DAOModule {
    @Provides
    @Singleton
    public DAOUser getDAOUser() {
        return new DAOUserImpl();
    }

    @Provides
    @Singleton
    public DAOPet getDAOPet() {
        return new DAOPetImpl();
    }
}
