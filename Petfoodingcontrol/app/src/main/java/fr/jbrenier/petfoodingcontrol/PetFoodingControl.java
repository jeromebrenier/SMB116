package fr.jbrenier.petfoodingcontrol;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import fr.jbrenier.petfoodingcontrol.di.DaggerRepositoryComponent;
import fr.jbrenier.petfoodingcontrol.di.RepositoryComponent;

/**
 * The Pet Fooding Control application class.
 * @author Jérôme Brenier
 */
public class PetFoodingControl extends Application {

    /** LOGGING */
    private static final String TAG = "PetFoodingControl";

    private RepositoryComponent repositoryComponent;
    private SharedPreferences appSharedPreferences;
    public MutableLiveData<Boolean> isCameraPermissionGranted = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isReadExternalStoragePermissionGranted =
            new MutableLiveData<>(false);

    @Override
    public void onCreate() {
        super.onCreate();
        repositoryComponent = DaggerRepositoryComponent.builder().application(this).build();
        loadPreferences();
    }

    /**
     * Load application-wide shared preferences.
     */
    private void loadPreferences() {
        if (appSharedPreferences == null) {
            Log.i(TAG, "Loading preferences.");
            appSharedPreferences = getSharedPreferences(
                    getString(R.string.application_preferences), Context.MODE_PRIVATE);
        }
    }

    public RepositoryComponent getRepositoryComponent() {
        return repositoryComponent;
    }

    public SharedPreferences getAppSharedPreferences() {
        return appSharedPreferences;
    }
}
