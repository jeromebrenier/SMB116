package fr.jbrenier.petfoodingcontrol.android.application;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import fr.jbrenier.petfoodingcontrol.di.app.AppComponent;
import fr.jbrenier.petfoodingcontrol.di.app.DaggerAppComponent;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;

/**
 * The Pet Fooding Control application class.
 * @author Jérôme Brenier
 */
public class PetFoodingControl extends Application {

    /** Logging */
    private static final String TAG = "PetFoodingControl";

    private final MutableLiveData<User> userLogged = new MutableLiveData<>();

    private AppComponent appComponent;
    public final MutableLiveData<Boolean> isCameraPermissionGranted =
            new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> isReadExternalStoragePermissionGranted =
            new MutableLiveData<>(false);

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Application start");
        appComponent = DaggerAppComponent.builder().application(this).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public MutableLiveData<User> getUserLogged() {
        return userLogged;
    }
}
