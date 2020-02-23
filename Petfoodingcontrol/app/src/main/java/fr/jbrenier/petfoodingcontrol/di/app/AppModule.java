package fr.jbrenier.petfoodingcontrol.di.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import fr.jbrenier.petfoodingcontrol.android.application.PetFoodingControl;

/**
 * Dagger Dependency Injection :  App Module
 * @author Jérôme Brenier
 */
@Module
class AppModule {
    private static final String PREFERENCES = "pfc-preferences";

    @Provides
    PetFoodingControl getPetFoodingControl(Application application) {
        return (PetFoodingControl) application;
    }

    @Provides
    SharedPreferences getSharedPreferences(Application application) {
        return application.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }
}
