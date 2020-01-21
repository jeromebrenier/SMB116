package fr.jbrenier.petfoodingcontrol.di.services;

import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.services.photoservice.PhotoService;
import fr.jbrenier.petfoodingcontrol.services.photoservice.PhotoServiceImpl;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserServiceImpl;

@Module
public class ServicesModule {

    @Provides
    public UserService getUserService(PetFoodingControlRepository pfcRepository,
                                      SharedPreferences sharedPreferences) {
        return new UserServiceImpl(pfcRepository, sharedPreferences);
    }

    @Provides
    public PhotoService getPhotoService(PetFoodingControlRepository pfcRepository,
                                        SharedPreferences sharedPreferences,
                                        UserService userService) {
        return new PhotoServiceImpl(pfcRepository, sharedPreferences, userService);
    }
}
