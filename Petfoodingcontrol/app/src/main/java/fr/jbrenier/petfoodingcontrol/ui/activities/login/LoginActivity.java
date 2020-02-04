package fr.jbrenier.petfoodingcontrol.ui.activities.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;
import fr.jbrenier.petfoodingcontrol.ui.fragments.login.LoginFieldsFragment;
import fr.jbrenier.petfoodingcontrol.ui.fragments.login.LoginWelcomeFragment;
import fr.jbrenier.petfoodingcontrol.utils.InputValidationUtils;

/**
 * The Login activity of the Pet Fooding Control application.
 * @author Jérôme Brenier
 */
public class LoginActivity extends AppCompatActivity {

    /** LOGGING */
    private static final String TAG = "LoginActivity";

    @Inject
    UserService userService;

    private PetFoodingControl petFoodingControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        petFoodingControl = (PetFoodingControl) getApplication();
        petFoodingControl.getAppComponent().inject(this);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            setupUserLoggedListener();
        }
        userService.initLogin(this);
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
        } else if (!InputValidationUtils.isEmailValid(email)) {
            showToast(R.string.toast_email_invalid);
        } else {
            tryToLog(email, password,
                    ((CheckBox) findViewById(R.id.chk_keep_logged_in)).isChecked());
        }
    }

    /**
     * Display a toast with a message corresponding to the resource given in parameter.
     */
    private void showToast(int resId) {
        Toast toast = Toast.makeText(this, resId, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Try to log with the data provided (email, password and keep logged).
     * @param email the email entered
     * @param password the password entered
     * @param isKeepLogged true if "keep logged" checked, false otherwise
     */
    private void tryToLog(String email, String password, boolean isKeepLogged) {
        userService.tryToLog(this, email, password, isKeepLogged).observe(this,
                result -> {
                    if (result == 1) {
                        showToast(R.string.toast_failed_login_wrong_password);
                    } else if (result == 2) {
                        showToast(R.string.toast_failed_login_user_unknown);
                    }
                });
    }

    /**
     * Setup a listener to load the login fields if no user is logged or the welcome fragment
     * in the contrary.
     */
    private void setupUserLoggedListener() {
        petFoodingControl.getUserLogged().observe(this, user -> {
            if (user == null) {
                loadFragment(new LoginFieldsFragment());
            } else {
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
        userService.clearDisposables(this);
        super.onDestroy();
    }

    public UserService getUserService() {
        return userService;
    }
}
