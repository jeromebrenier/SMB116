package fr.jbrenier.petfoodingcontrol.ui.activities.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;
import fr.jbrenier.petfoodingcontrol.ui.fragments.login.LoginFieldsFragment;
import fr.jbrenier.petfoodingcontrol.ui.fragments.login.LoginWelcomeFragment;

/**
 * The Login activity of the Pet Fooding Control application.
 * @author Jérôme Brenier
 */
public class LoginActivity extends AppCompatActivity {

    /** LOGGING */
    private static final String TAG = "LoginActivity";

    @Inject
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PetFoodingControl) getApplicationContext()).getServicesComponent().inject(this);
        setContentView(R.layout.activity_login);
        userService.initLogin();
        if (savedInstanceState == null) {
            setupUserLoggedListener();
        }
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
        userService.tryToLog(email, password, isKeepLogged).observe(this, result -> {
            if (result == 1) {
                showToast(R.string.toast_failed_login);
            } else if (result == 0) {
                showToast(R.string.toast_success_login);
            }});
    }

    /**
     * Setup a listener to load the login fields if no user is logged or the welcome fragment
     * in the contrary.
     */
    private void setupUserLoggedListener() {
        userService.getPfcRepository().getUserLogged().observe(this, user -> {
            if (user == null) {
                loadFragment(LoginFieldsFragment.newInstance());
            } else {
                loadFragment(LoginWelcomeFragment.newInstance());
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
        super.onDestroy();
    }
}
