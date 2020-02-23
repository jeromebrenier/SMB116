package fr.jbrenier.petfoodingcontrol.di.app;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import fr.jbrenier.petfoodingcontrol.di.repository.RepositoryModule;
import fr.jbrenier.petfoodingcontrol.di.services.ServicesModule;
import fr.jbrenier.petfoodingcontrol.services.petservice.PetService;
import fr.jbrenier.petfoodingcontrol.services.photoservice.PhotoService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;
import fr.jbrenier.petfoodingcontrol.android.activities.accountcreation.AccountCreationActivity;
import fr.jbrenier.petfoodingcontrol.android.activities.login.LoginActivity;
import fr.jbrenier.petfoodingcontrol.android.activities.main.MainActivityViewModel;
import fr.jbrenier.petfoodingcontrol.android.activities.petfooding.PetFoodingViewModel;
import fr.jbrenier.petfoodingcontrol.android.activities.petmanagement.PetManagementViewModel;
import fr.jbrenier.petfoodingcontrol.android.fragments.accountmanagement.AccountCreationFormFragment;

/**
 * Dagger Dependency Injection :  App Component
 * @author Jérôme Brenier
 */
@Singleton
@Component(modules = {AppModule.class, RepositoryModule.class, ServicesModule.class})
public interface AppComponent {
    void inject(LoginActivity loginActivity);
    void inject(AccountCreationActivity accountCreationActivity);
    void inject(MainActivityViewModel mainActivityViewModel);
    void inject(AccountCreationFormFragment accountCreationFormFragment);
    void inject(PetManagementViewModel petManagementViewModel);
    void inject(PetFoodingViewModel petFoodingViewModel);

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
