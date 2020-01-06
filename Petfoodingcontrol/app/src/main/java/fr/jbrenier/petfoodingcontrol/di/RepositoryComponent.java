package fr.jbrenier.petfoodingcontrol.di;

import javax.inject.Singleton;

import dagger.Component;
import fr.jbrenier.petfoodingcontrol.ui.activities.login.LoginActivity;
import fr.jbrenier.petfoodingcontrol.ui.activities.main.MainActivity;
import fr.jbrenier.petfoodingcontrol.ui.fragments.login.LoginWelcomeFragment;

@Singleton
@Component(modules={RepositoryModule.class})
public interface RepositoryComponent {
    void inject(LoginActivity loginActivity);
    void inject(MainActivity mainActivity);
    void inject(LoginWelcomeFragment loginWelcomeFragment);
}
