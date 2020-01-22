package fr.jbrenier.petfoodingcontrol.di.services;

import dagger.Component;
import fr.jbrenier.petfoodingcontrol.di.app.AppComponent;
import fr.jbrenier.petfoodingcontrol.di.repository.RepositoryComponent;
import fr.jbrenier.petfoodingcontrol.ui.activities.accountcreation.AccountCreationActivity;
import fr.jbrenier.petfoodingcontrol.ui.activities.login.LoginActivity;
import fr.jbrenier.petfoodingcontrol.ui.activities.main.MainActivity;
import fr.jbrenier.petfoodingcontrol.ui.fragments.main.pets.PetFragment;

@Component(dependencies = {AppComponent.class, RepositoryComponent.class},
        modules = ServicesModule.class)
@ServicesScope
public interface ServicesComponent {
    void inject(LoginActivity loginActivity);
    void inject(AccountCreationActivity accountCreationActivity);
    void inject(MainActivity mainActivity);
    void inject(PetFragment petFragment);
}
