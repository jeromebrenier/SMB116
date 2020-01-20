package fr.jbrenier.petfoodingcontrol.ui.activities.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

import java.time.OffsetDateTime;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.user.AutoLogin;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.ui.fragments.login.LoginFieldsFragment;
import fr.jbrenier.petfoodingcontrol.ui.fragments.login.LoginWelcomeFragment;
import fr.jbrenier.petfoodingcontrol.utils.AutoLoginUtils;
import fr.jbrenier.petfoodingcontrol.utils.CryptographyUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * The Login activity of the Pet Fooding Control application.
 * @author Jérôme Brenier
 */
public class LoginActivity extends AppCompatActivity {

    /** LOGGING */
    private static final String TAG = "LoginActivity";

    @Inject
    PetFoodingControlRepository pfcRepository;

    private SharedPreferences sharedPref;

    /** Manages disposables */
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PetFoodingControl) getApplicationContext()).getRepositoryComponent().inject(this);
        setContentView(R.layout.activity_login);
        isKeepLogged();
        if (savedInstanceState == null) {
            loadFragment(pfcRepository.getUserLogged().getValue() == null ?
                    LoginFieldsFragment.newInstance() : LoginWelcomeFragment.newInstance());
        }
    }

    /**
     * Check if the "Keep me logged" feature is active (i.e if an email and a hashed password is
     * stored in the preferences). If true return the User corresponding to the credentials. If
     * false or if the credentials are invalid return null.
     */
    private void isKeepLogged() {
        loadSharedPref();
        String autoLogin = sharedPref.getString(getString(R.string.autologin_token_id),"");
        if (!autoLogin.isEmpty()) {
            Log.i(TAG, "Auto login value stored in preferences : " + autoLogin);
            tryAutologin(autoLogin);
        }
    }

    /**
     * Lazy load the sharedPref.
     */
    private void loadSharedPref() {
        if (sharedPref == null) {
            sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        }
    }

    /**
     * Try to log with auto login with the local token given as a parameter.
     * @param autoLoginLocalToken the local auto login token
     */
    private void tryAutologin(String autoLoginLocalToken) {
        Disposable disposable = pfcRepository.getUserByAutoLogin(autoLoginLocalToken).subscribe(
                user -> {
                    Log.i(TAG, "User from AutoLogin successfully retrieved");
                    setupUserLoggedListener();
                    pfcRepository.setUserLogged(user);
                }, throwable ->
                        Log.e(TAG, "AutoLogin failed", throwable));
        compositeDisposable.add(disposable);
    }

    /**
     * Check credentials (email and password). Return an User or null if credentials invalid.
     * @param email entered
     * @param password entered
     */
    private void checkCredentials(String email, String password) {
        Disposable disposable = pfcRepository.getUserByEmail(email).subscribe(
                user -> CryptographyUtils.checkPassword(password, user.getPassword()).subscribe(
                        () -> {
                            pfcRepository.setUserLogged(user);
                            manageAutologinForUser(user);
                        }),
                throwable -> {
                    showToast(R.string.toast_failed_login);
                    Log.e(TAG, "checkCredentials failure ", throwable);
                });
        compositeDisposable.add(disposable);
    }

    private void manageAutologinForUser(User user) {
        if (((CheckBox) findViewById(R.id.chk_keep_logged_in)).isChecked()) {
            AutoLogin autoLogin = new AutoLogin(
                    AutoLoginUtils.getInstance().getUuid().toString(),
                    OffsetDateTime.now().plusWeeks(1L),
                    user.getUserId()
            );
            Disposable disposable = pfcRepository.insert(autoLogin).subscribe(
                    () -> {
                        Log.i(TAG, "AutoLogin for user " + user.getUserId() +
                                " successfully inserted");
                        storeAutologinInPreferences(autoLogin);
                    },
                    throwable -> Log.e(TAG, "AutoLogin failed to be inserted for user " +
                                    user.getUserId()));
            compositeDisposable.add(disposable);
        }
    }


    private void storeAutologinInPreferences(AutoLogin autoLogin) {
        loadSharedPref();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.autologin_token_id), autoLogin.getTokenId());
        editor.commit();
        Log.i(TAG,"Auto login successfully stored in the preferences");
    }
    /**
     * Display a toast with a message corresponding to the resource given in parameter.
     */
    private void showToast(int resId) {
        Toast toast = Toast.makeText(this, resId, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Charge le fragment passé en paramètre
     * @param fragment à charger
     */
    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.login_frameLayout, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Action invoked when login button is clicked
     * @param email entered
     * @param password entered
     */
    public void onLoginButtonClick(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            showToast(R.string.toast_input_empty);
        } else {
            checkCredentials(email, password);
            setupUserLoggedListener();
        }
    }

    /**
     * Setup a listener to load the welcome fragment once a user is effectively logged.
     */
    private void setupUserLoggedListener() {
        pfcRepository.getUserLogged().observe(this, user -> {
            if (user != null) {
                loadFragment(new LoginWelcomeFragment());
            }
        });
    }

    /**
     * Return to the Main activity, and finishes the Login activity.
     */
    public void finishLoginActivity(int resultCode) {
        Intent retIntent = new Intent();
        setResult(resultCode, retIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    public PetFoodingControlRepository getPetFoodingControlRepository() {
        return pfcRepository;
    }
}
