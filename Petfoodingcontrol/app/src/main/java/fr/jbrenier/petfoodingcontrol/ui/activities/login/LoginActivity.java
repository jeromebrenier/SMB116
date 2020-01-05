package fr.jbrenier.petfoodingcontrol.ui.activities.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.repository.UserRepository;
import fr.jbrenier.petfoodingcontrol.ui.fragments.login.LoginFieldsFragment;
import fr.jbrenier.petfoodingcontrol.ui.fragments.login.LoginWelcomeFragment;

/**
 * The Login activity of the Pet Fooding Control application.
 * @author Jérôme Brenier
 */
public class LoginActivity extends AppCompatActivity {

    @Inject
    UserRepository userRepository;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PetFoodingControl) getApplicationContext()).getRepositoryComponent().inject(this);
        setContentView(R.layout.activity_login);
        userRepository.setUserLogged(isKeepLogged());
        if (userRepository.getUserLogged().getValue() == null) {
            loadFragment(new LoginFieldsFragment());
        } else {
            loadFragment(new LoginWelcomeFragment());
        }
    }

    /**
     * Check if the "Keep me logged" feature is active (i.e if an email and a hashed password is
     * stored in the preferences). If true return the User corresponding to the credentials. If
     * false or if the credentials are invalid return null.
     * @return the User or null if no data or invalid data
     */
    private User isKeepLogged() {
        loadSharedPref();
        String email = sharedPref.getString(this.getResources().getString(R.string.saved_email),
                "");
        String password = sharedPref.getString(
                this.getResources().getString(R.string.saved_password),"");
        if (email.equals("") || password.equals("")) {
            return null;
        }
        return checkCredentials(email, password);
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
     * Check credentials (email and password). Return an User or null if credentials invalid.
     * @param email
     * @param password
     * @return corresponding User
     */
    private User checkCredentials(String email, String password) {
        return userRepository.getByCredentials(email, password);
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
            sendToastInputEmpty();
        } else {
            onCredentialsEntered(email, password);
        }
    }

    /**
     * Action when credentials have been entered and validated.
     * @param inputEmail email entered
     * @param inputPassword password entered
     */
    private void onCredentialsEntered(String inputEmail, String inputPassword) {
        userRepository.setUserLogged(checkCredentials(inputEmail, inputPassword));
        if (userRepository.getUserLogged().getValue() != null) {
            loadFragment(new LoginWelcomeFragment());
        }
    }

    /**
     * Display a toast alerting that at least one of the fields is empty.
     */
    private void sendToastInputEmpty() {
        Toast toast = Toast.makeText(this, R.string.toast_input_empty, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Return to the Main activity, and finishes the Login activity.
     */
    public void finishLoginActivity(int resultCode) {
        Intent retIntent = new Intent();
        setResult(resultCode, retIntent);
        finish();
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
