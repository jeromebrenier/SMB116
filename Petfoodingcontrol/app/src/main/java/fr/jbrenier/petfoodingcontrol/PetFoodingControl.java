package fr.jbrenier.petfoodingcontrol;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import fr.jbrenier.petfoodingcontrol.di.app.AppComponent;
import fr.jbrenier.petfoodingcontrol.di.app.DaggerAppComponent;

/**
 * The Pet Fooding Control application class.
 * @author Jérôme Brenier
 */
public class PetFoodingControl extends Application {

    /** LOGGING */
    private static final String TAG = "PetFoodingControl";

    private AppComponent appComponent;
    public MutableLiveData<Boolean> isCameraPermissionGranted = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isReadExternalStoragePermissionGranted =
            new MutableLiveData<>(false);

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().application(this).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
