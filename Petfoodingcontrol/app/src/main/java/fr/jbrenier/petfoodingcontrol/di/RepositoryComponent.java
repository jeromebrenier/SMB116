package fr.jbrenier.petfoodingcontrol.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import fr.jbrenier.petfoodingcontrol.ui.activities.accountcreation.AccountCreationActivity;
import fr.jbrenier.petfoodingcontrol.ui.activities.login.LoginActivity;
import fr.jbrenier.petfoodingcontrol.ui.activities.main.MainActivity;

@Singleton
@Component(modules={RepositoryModule.class})
public interface RepositoryComponent {
    void inject(LoginActivity loginActivity);
    void inject(MainActivity mainActivity);
    void inject(AccountCreationActivity accountCreationActivity);

    @Component.Builder
    interface Builder {

        RepositoryComponent build();

        @BindsInstance
        Builder application(Application application);
    }
}
