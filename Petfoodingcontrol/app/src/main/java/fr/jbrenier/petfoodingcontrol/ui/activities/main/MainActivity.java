package fr.jbrenier.petfoodingcontrol.ui.activities.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.services.petservice.PetService;
import fr.jbrenier.petfoodingcontrol.services.photoservice.PhotoService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserServiceKeysEnum;
import fr.jbrenier.petfoodingcontrol.ui.activities.login.LoginActivity;
import fr.jbrenier.petfoodingcontrol.ui.activities.petaddition.PetAdditionActivity;
import fr.jbrenier.petfoodingcontrol.ui.fragments.accountmanagement.AccountManagementFormFragment;
import fr.jbrenier.petfoodingcontrol.ui.fragments.main.pets.PetFragment;

/**
 * Main activity of the Pet Fooding Control application.
 * @author Jérôme Brenier
 */
public class MainActivity extends AppCompatActivity implements
        PetFragment.OnListFragmentInteractionListener,
        AccountManagementFormFragment.OnSaveButtonClickListener{

    private static final int LOGIN_REQUEST = 1;
    private static final int ADD_PET_REQUEST = 2;

    /** PERMISSIONS */
    private static final int PERMISSIONS_REQUEST = 3;
    private final CountDownLatch permissionLatch = new CountDownLatch(1);

    /** LOGGING */
    private static final String TAG = "MainActivity";

    @Inject
    UserService userService;

    @Inject
    PhotoService photoService;

    @Inject
    PetService petService;

    private TextView user_name;
    private TextView user_email;
    private ImageView user_photo;
    private Toolbar toolbar;
    private AppBarConfiguration mAppBarConfiguration;
    private View headerView;

    private PetFoodingControl petFoodingControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        petFoodingControl = (PetFoodingControl) getApplication();
        petFoodingControl.getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        setupAddButton();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_pets, R.id.nav_account_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        headerView = navigationView.getHeaderView(0);
        getUserDataView();
        setupLogoutListener();
        setupUserLoggedListener();
        checkApplicationPermissions();
        armPermissionLatch();
        if (userService.getPfcRepository().getUserLogged().getValue() == null) {
            Log.i(TAG, "Launching login activity.");
            launchLoginActivity();
        }
    }

    /**
     * Check the application permissions.
     */
    private void checkApplicationPermissions() {
        Log.i(TAG, "Checking permissions.");
        final List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();

        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add(getResources().getString(R.string.permission_camera));
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add(getResources().
                    getString(R.string.permission_read_external_storage));

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = getResources().getString(R.string.permission_grant_access_needed)
                        + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        (dialog, which) -> requestPermissions(permissionsList.toArray(
                                new String[permissionsList.size()]), PERMISSIONS_REQUEST),
                        (dialog, which) -> permissionLatch.countDown());
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    PERMISSIONS_REQUEST);
            return;
        }
        permissionLatch.countDown();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            if (!shouldShowRequestPermissionRationale(permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Show an OK/Cancel dialog message to inform that a permission is needed and ask for a
     * reaction.
     * @param message the message to display
     * @param okListener the listener triggered by the OK button
     */
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener,
                                     DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton(getResources().getString(R.string.btn_OK), okListener)
                .setNegativeButton(getResources().getString(R.string.btn_cancel), cancelListener)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                Map<String, Integer> perms = new HashMap<String, Integer>();

                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    petFoodingControl.isCameraPermissionGranted.setValue(true);
                } else if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    petFoodingControl.isReadExternalStoragePermissionGranted.setValue(true);
                }
                permissionLatch.countDown();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void armPermissionLatch() {
        try {
            permissionLatch.await();
        } catch (InterruptedException e) {
            Log.i(TAG, "PermissionLatch.await() error : " + e.getMessage());
        }
    }

    /**
     * Setup user logged listener to populate userPets accordingly and to display its data in
     * the header.
     */
    private void setupUserLoggedListener() {
        userService.getPfcRepository().getUserLogged().observe(this, user -> {
            if (user != null) {
                setUserPets(user);
                setUserDataInNavBar(user);
                Log.i(TAG, "User logged changed to " + user.getUserId().toString());
            } else {
                Log.i(TAG, "User logged changed to null");
            }
         });
    }

    /**
     * Launch the populate of the User's pet list (owned and authorized to fed).
     * @param user the logged user
     */
    private void setUserPets(User user) {
        petService.setUserPets(user);
    }

    /**
     * Set the User elements (name, email, photo) in the navigation bar dedicated area according to
     * the User data.
     * @param user : the logged User
     */
    private void setUserDataInNavBar(User user) {
        user_name.setText(user == null ? "********" : user.getDisplayedName());
        user_email.setText(user == null ? "********" : user.getEmail());
        if (user != null) {
        photoService.get(this, user).observe(this, bitmap -> {
            if (bitmap != null) {
                user_photo.setImageBitmap(bitmap);
            }
        });
        } else {

        }
    }

    /**
     * Setup the add pet button
     */
    private void setupAddButton() {
        FloatingActionButton addPet = findViewById(R.id.main_addPet);
        addPet.setOnClickListener(view -> sendPetAdditionActivityIntent());
    }

    private void sendPetAdditionActivityIntent() {
        Intent petAdditionActivityIntent = new Intent(this, PetAdditionActivity.class);
        startActivityForResult(petAdditionActivityIntent, ADD_PET_REQUEST);
    }

    /**
     * Get views from the header used to display the User data.
     */
    private void getUserDataView() {
        user_name = headerView.findViewById(R.id.txt_user_name);
        user_email = headerView.findViewById(R.id.txt_user_email);
        user_photo = headerView.findViewById(R.id.imv_user_photo);
    }

    /**
     * Set the logout button listener to invoke log out on click.
     */
    private void setupLogoutListener() {
        headerView.findViewById(R.id.btn_log_out).setOnClickListener(view -> logout());
    }

    /**
     * Log out.
     */
    private void logout() {
        userService.logout();
        launchLoginActivity();
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Launch login activity trough explicit intent.
     */
    public void launchLoginActivity() {
        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(loginActivityIntent, LOGIN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST && resultCode != RESULT_OK) {
            finishAndRemoveTask();
        }
    }

    /**
     * Set the correct localized toolbar title.
     */
    public void setToolBarTitle(int stringId) {
        toolbar.setTitle(getResources().getString(stringId));
    }

    @Override
    protected void onDestroy() {
        userService.clearDisposables(this);
        photoService.clearDisposables(this);
        userService.leave();
        super.onDestroy();
    }

    @Override
    public void onListFragmentInteraction(Pet pet) {

    }

    @Override
    public void onSaveButtonClick(Map<UserServiceKeysEnum, String> userData) {
        userService.update(this, userData).observe(this, result -> {
            if (result == 1) {
                showToast(getResources().getString(R.string.toast_account_update_failure));
            } else if (result == 0) {
                updateUserPhoto(userData);
            }
        });
    }

    /**
     * Update the user's photo.
     * @param userData the user's data retrieved from the input
     */
    private void updateUserPhoto(Map<UserServiceKeysEnum, String> userData) {
        photoService.update(this, userData).observe(this, updateResult -> {
            if (updateResult == 1) {
                showToast(getResources().getString(R.string.toast_account_update_failure));
            } else if (updateResult == 0) {
                showToast(getResources().getString(R.string.toast_account_update_success));
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
}