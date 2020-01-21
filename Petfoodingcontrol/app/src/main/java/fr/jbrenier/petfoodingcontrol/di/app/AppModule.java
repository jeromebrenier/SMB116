package fr.jbrenier.petfoodingcontrol.di.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private static final String PREFERENCES = "pfc-preferences";

    @Provides
    public SharedPreferences getSharedPreferences(Application application) {
        return application.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }
}
