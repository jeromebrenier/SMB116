package fr.jbrenier.petfoodingcontrol.di.services;

import dagger.Component;
import fr.jbrenier.petfoodingcontrol.di.app.AppComponent;
import fr.jbrenier.petfoodingcontrol.di.repository.RepositoryComponent;
import fr.jbrenier.petfoodingcontrol.ui.activities.login.LoginActivity;

@Component(dependencies = {AppComponent.class, RepositoryComponent.class},
        modules = ServicesModule.class)
@ServicesScope
public interface ServicesComponent {
    void inject(LoginActivity loginActivity);
}
