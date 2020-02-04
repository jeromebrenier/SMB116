package fr.jbrenier.petfoodingcontrol.di.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import fr.jbrenier.petfoodingcontrol.PetFoodingControl;

@Module
public class AppModule {
    private static final String PREFERENCES = "pfc-preferences";

    @Provides
    public PetFoodingControl getPetFoodingControl(Application application) {
        return (PetFoodingControl) application;
    }

    @Provides
    public SharedPreferences getSharedPreferences(Application application) {
        return application.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }
}
