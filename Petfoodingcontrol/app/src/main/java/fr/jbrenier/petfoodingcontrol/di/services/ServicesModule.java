package fr.jbrenier.petfoodingcontrol.di.services;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fr.jbrenier.petfoodingcontrol.android.application.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.services.petservice.PetService;
import fr.jbrenier.petfoodingcontrol.services.petservice.PetServiceImpl;
import fr.jbrenier.petfoodingcontrol.services.photoservice.PhotoService;
import fr.jbrenier.petfoodingcontrol.services.photoservice.PhotoServiceImpl;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserServiceImpl;

/**
 * Dagger Dependency Injection :  Services Module
 * @author Jérôme Brenier
 */
@Module
public class ServicesModule {

    @Singleton
    @Provides
    UserService getUserService(PetFoodingControl petFoodingControl,
                                      PetFoodingControlRepository pfcRepository,
                                      SharedPreferences sharedPreferences) {
        return new UserServiceImpl(petFoodingControl, pfcRepository, sharedPreferences);
    }

    @Singleton
    @Provides
    PetService getPetService(PetFoodingControlRepository pfcRepository) {
        return new PetServiceImpl(pfcRepository);
    }

    @Singleton
    @Provides
    PhotoService getPhotoService(PetFoodingControlRepository pfcRepository,
                                        UserService userService,
                                        PetService petService) {
        return new PhotoServiceImpl(pfcRepository, userService, petService);
    }
}
