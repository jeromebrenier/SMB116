package fr.jbrenier.petfoodingcontrol.di.repository;

import android.app.Application;

import dagger.BindsInstance;
import dagger.Component;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;

@RepositoryScope
@Component(modules={RepositoryModule.class})
public interface RepositoryComponent {

    PetFoodingControlRepository petFoodingControlRepository();

    @Component.Builder
    interface Builder {

        RepositoryComponent build();

        @BindsInstance
        RepositoryComponent.Builder application(Application application);
    }
}
