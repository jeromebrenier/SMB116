package fr.jbrenier.petfoodingcontrol.di.app;

import android.app.Application;
import android.content.SharedPreferences;

import dagger.BindsInstance;
import dagger.Component;

@Component(modules = AppModule.class)
public interface AppComponent {

    SharedPreferences sharedPreferences();

    @Component.Builder
    interface Builder {

        AppComponent build();

        @BindsInstance
        AppComponent.Builder application(Application application);
    }
}
