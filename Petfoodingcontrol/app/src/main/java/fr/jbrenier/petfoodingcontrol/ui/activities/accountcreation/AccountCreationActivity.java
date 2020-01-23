package fr.jbrenier.petfoodingcontrol.ui.activities.accountcreation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Map;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.services.photoservice.PhotoService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserServiceKeysEnum;
import fr.jbrenier.petfoodingcontrol.ui.fragments.accountmanagement.AccountManagementFormFragment;
import fr.jbrenier.petfoodingcontrol.utils.CryptographyUtils;

/**
 * Activity for creating User accounts.
 * @author Jérôme Brenier
 */
public class AccountCreationActivity extends AppCompatActivity
        implements AccountManagementFormFragment.OnSaveButtonClickListener {

    private static final String DUMMY_TITLE = " ";
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 1;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 2;

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
        // Camera permission management
        petFoodingControl.isCameraPermissionGranted.setValue(
                checkPermission(Manifest.permission.CAMERA, REQUEST_CODE_CAMERA_PERMISSION));
        // Storage access permission management
        petFoodingControl.isReadExternalStoragePermissionGranted.setValue(
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                        REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION));
        if (savedInstanceState == null) {
            loadFragment(AccountManagementFormFragment.newInstance());
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

    /**
     * Check if the permission has been granted, and if negative, request it.
     * @param permission needed
     * @param requestCode code of the request
     */
    private boolean checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { permission }, requestCode);
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CAMERA_PERMISSION :
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    petFoodingControl.isCameraPermissionGranted.setValue(true);
                } else {
                    petFoodingControl.isCameraPermissionGranted.setValue(false);
                }
                break;
            case REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION :
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    petFoodingControl.isReadExternalStoragePermissionGranted.setValue(true);
                } else {
                    petFoodingControl.isReadExternalStoragePermissionGranted.setValue(false);
                }
                break;
            default :
                break;
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof AccountManagementFormFragment) {
            AccountManagementFormFragment accountManagementFormFragment =
                    (AccountManagementFormFragment) fragment;
            accountManagementFormFragment.setCallback(this);
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

        userService.save(this, newUser).observe(this, result -> {
            if (result == 0) {
                if (base64photo != null ) {
                    final Photo userPhoto = new Photo(base64photo);
                    photoService.save(this, userPhoto, newUser);
                }
                showToast(getResources().getString(R.string.toast_account_created));
                finish();
            } else if (result == 1) {
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
