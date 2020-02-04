package fr.jbrenier.petfoodingcontrol.ui.activities.accountcreation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import java.util.Map;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.entities.photo.Photo;
import fr.jbrenier.petfoodingcontrol.entities.user.User;
import fr.jbrenier.petfoodingcontrol.services.photoservice.PhotoService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserServiceKeysEnum;
import fr.jbrenier.petfoodingcontrol.ui.fragments.accountmanagement.AccountCreationFormFragment;
import fr.jbrenier.petfoodingcontrol.utils.CryptographyUtils;

/**
 * Activity for creating User accounts.
 * @author Jérôme Brenier
 */
public class AccountCreationActivity extends AppCompatActivity
        implements AccountCreationFormFragment.OnSaveButtonClickListener {

    private static final String DUMMY_TITLE = " ";

    /** Logging */
    private static final String TAG = "AccountCreationActivity";

    private PetFoodingControl petFoodingControl;

    @Inject
    UserService userService;

    @Inject
    PhotoService photoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        petFoodingControl = ((PetFoodingControl) getApplication());
        setContentView(R.layout.activity_account_creation);
        setActivityTitle();
        petFoodingControl.getAppComponent().inject(this);
        if (savedInstanceState == null) {
            loadFragment(new AccountCreationFormFragment());
        }
    }

    /**
     * Set the correct title in the toolbar.
     */
    private void setActivityTitle() {
        Toolbar toolbar = findViewById(R.id.account_management_toolbar);
        // workaround to get the setTitle method really work afterward
        toolbar.setTitle(DUMMY_TITLE);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_account_creation));
    }

    /**
     * Charge le fragment passé en paramètre
     * @param fragment à charger
     */
    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.account_management_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof AccountCreationFormFragment) {
            AccountCreationFormFragment accountCreationFormFragment =
                    (AccountCreationFormFragment) fragment;
            accountCreationFormFragment.setCallback(this);
        }
    }

    @Override
    public void onSaveButtonClick(Map<UserServiceKeysEnum, String> userData) {
        // USER
        String hashedUserPassword = CryptographyUtils.hashPassword(
                userData.get(UserServiceKeysEnum.PASSWORD_KEY));
        final User newUser = new User(
                userData.get(UserServiceKeysEnum.USERNAME_KEY),
                userData.get(UserServiceKeysEnum.EMAIL_KEY),
                hashedUserPassword,
                null
        );
        // PHOTO
        String base64photo = userData.get(UserServiceKeysEnum.PHOTO_KEY);

        userService.save(this, newUser).observe(this, userCreated -> {
            if (userCreated != null) {
                if (base64photo != null ) {
                    final Photo userPhoto = new Photo(base64photo);
                    photoService.save(this, userPhoto, userCreated);
                }
                showToast(getResources().getString(R.string.toast_account_created));
                finish();
            } else {
                showToast(getResources().getString(R.string.toast_account_not_created));
            }
        });
    }

    /**
     * Create and show a toast.
     * @param message text to show
     */
    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        userService.clearDisposables(this);
        super.onDestroy();
    }
}
