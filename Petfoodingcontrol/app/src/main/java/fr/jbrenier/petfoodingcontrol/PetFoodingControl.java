package fr.jbrenier.petfoodingcontrol;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import fr.jbrenier.petfoodingcontrol.di.app.AppComponent;
import fr.jbrenier.petfoodingcontrol.di.app.DaggerAppComponent;
import fr.jbrenier.petfoodingcontrol.di.repository.DaggerRepositoryComponent;
import fr.jbrenier.petfoodingcontrol.di.repository.RepositoryComponent;
import fr.jbrenier.petfoodingcontrol.di.services.ServicesComponent;

/**
 * The Pet Fooding Control application class.
 * @author Jérôme Brenier
 */
public class PetFoodingControl extends Application {

    /** LOGGING */
    private static final String TAG = "PetFoodingControl";

    private AppComponent appComponent;
    private RepositoryComponent repositoryComponent;
    private ServicesComponent servicesComponent;
    public MutableLiveData<Boolean> isCameraPermissionGranted = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isReadExternalStoragePermissionGranted =
            new MutableLiveData<>(false);

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().application(this).build();
        repositoryComponent = DaggerRepositoryComponent.builder().application(this).build();
        servicesComponent = DaggerUserServiceComponent.builder()
                .appComponent(appComponent)
                .repositoryComponent(repositoryComponent).build();
    }

    public ServicesComponent getServicesComponent() {
        return servicesComponent;
    }

}
