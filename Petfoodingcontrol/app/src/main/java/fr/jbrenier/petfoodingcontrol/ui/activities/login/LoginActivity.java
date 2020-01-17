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
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.ui.fragments.login.LoginFieldsFragment;
import fr.jbrenier.petfoodingcontrol.ui.fragments.login.LoginWelcomeFragment;
import fr.jbrenier.petfoodingcontrol.utils.CryptographyUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * The Login activity of the Pet Fooding Control application.
 * @author Jérôme Brenier
 */
public class LoginActivity extends AppCompatActivity {

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
        String email = sharedPref.getString(this.getResources().getString(R.string.saved_email),
                "");
        String password = sharedPref.getString(
                this.getResources().getString(R.string.saved_password),"");
        if (email.equals("") || password.equals("")) {
            return;
        }
        checkCredentials(email, password);
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
     * @param email entered
     * @param password entered
     */
    private void checkCredentials(String email, String password) {
        Disposable disposable = pfcRepository.getUserByEmail(email).subscribe(
                user -> CryptographyUtils.checkPassword(password, user.getPassword()).subscribe(
                        () -> pfcRepository.setUserLogged(user))
                );
        compositeDisposable.add(disposable);
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
        checkCredentials(inputEmail, inputPassword);
        pfcRepository.getUserLogged().observe(this, user -> {
            if (user != null) {
                loadFragment(new LoginWelcomeFragment());
            }
        });
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

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    public PetFoodingControlRepository getPetFoodingControlRepository() {
        return pfcRepository;
    }
}
