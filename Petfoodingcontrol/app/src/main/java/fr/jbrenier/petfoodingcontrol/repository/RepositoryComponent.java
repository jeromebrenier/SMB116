package fr.jbrenier.petfoodingcontrol.repository;

import javax.inject.Singleton;

import dagger.Component;
import fr.jbrenier.petfoodingcontrol.ui.activities.login.LoginActivity;

@Singleton
@Component(modules={RepositoryModule.class})
public interface RepositoryComponent {
    void inject(LoginActivity loginActivity);
}
