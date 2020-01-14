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

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.ui.fragments.accountmanagement.AccountManagementFormFragment;

/**
 * Activity for creating User accounts.
 * @author Jérôme Brenier
 */
public class AccountCreationActivity extends AppCompatActivity {

    private static final String DUMMY_TITLE = " ";
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 1;

    @Inject
    PetFoodingControlRepository pfcRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);
        setActivityTitle();
        ((PetFoodingControl) getApplicationContext()).getRepositoryComponent().inject(this);
        checkPermission(Manifest.permission.CAMERA, REQUEST_CODE_CAMERA_PERMISSION);
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
    private void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { permission }, requestCode);
        } else {
            ((PetFoodingControl) getApplication()).isCameraPermissionGranted.setValue(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ((PetFoodingControl) getApplication()).isCameraPermissionGranted.setValue(true);
            } else {
                ((PetFoodingControl) getApplication()).isCameraPermissionGranted.setValue(false);
            }
            return;
        }
    }

    public PetFoodingControlRepository getPetFoodingControlRepository() {
        return pfcRepository;
    }
}