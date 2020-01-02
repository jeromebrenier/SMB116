package fr.jbrenier.petfoodingcontrol.db;

import javax.inject.Singleton;

import dagger.Component;
import fr.jbrenier.petfoodingcontrol.ui.activities.LoginActivity;

@Singleton
@Component (modules={DAOModule.class})
public interface DAOComponent {
    void inject(LoginActivity loginActivity);
}
