package fr.jbrenier.petfoodingcontrol.di.app;

import android.app.Application;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import fr.jbrenier.petfoodingcontrol.di.repository.RepositoryModule;
import fr.jbrenier.petfoodingcontrol.di.services.ServicesModule;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.services.petservice.PetService;
import fr.jbrenier.petfoodingcontrol.services.photoservice.PhotoService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;
import fr.jbrenier.petfoodingcontrol.ui.activities.accountcreation.AccountCreationActivity;
import fr.jbrenier.petfoodingcontrol.ui.activities.login.LoginActivity;
import fr.jbrenier.petfoodingcontrol.ui.activities.main.MainActivity;
import fr.jbrenier.petfoodingcontrol.ui.fragments.accountmanagement.AccountCreationFormFragment;
import fr.jbrenier.petfoodingcontrol.ui.fragments.main.pets.PetFragment;

@Singleton
@Component(modules = {AppModule.class, RepositoryModule.class, ServicesModule.class})
public interface AppComponent {
    void inject(LoginActivity loginActivity);
    void inject(AccountCreationActivity accountCreationActivity);
    void inject(MainActivity mainActivity);
    void inject(PetFragment petFragment);
    void inject(AccountCreationFormFragment accountCreationFormFragment);

    @Singleton
    SharedPreferences sharedPreferences();

    @Singleton
    PetFoodingControlRepository petFoodingControlRepository();

    @Singleton
    UserService getUserService();

    @Singleton
    PhotoService getPhotoService();

    @Singleton
    PetService getPetService();

    @Component.Builder
    interface Builder {

        AppComponent build();

        @BindsInstance
        AppComponent.Builder application(Application application);
    }
}
