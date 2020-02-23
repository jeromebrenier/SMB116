package fr.jbrenier.petfoodingcontrol.android.activities.main;

import android.content.Intent;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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

import java.util.List;
import java.util.Map;

import fr.jbrenier.petfoodingcontrol.android.application.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.android.extras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserServiceKeysEnum;
import fr.jbrenier.petfoodingcontrol.android.activities.login.LoginActivity;
import fr.jbrenier.petfoodingcontrol.android.activities.petfooding.PetFoodingActivity;
import fr.jbrenier.petfoodingcontrol.android.activities.petmanagement.PetCreationActivity;
import fr.jbrenier.petfoodingcontrol.android.fragments.accountmanagement.AccountCreationFormFragment;
import fr.jbrenier.petfoodingcontrol.android.fragments.main.pets.PetFragment;

/**
 * Main activity of the Pet Fooding Control application.
 * @author Jérôme Brenier
 */
public class MainActivity extends AppCompatActivity implements
        PetFragment.OnListFragmentInteractionListener,
        AccountCreationFormFragment.OnSaveButtonClickListener{

    /** Request code for Logging */
    private static final int LOGIN_REQUEST = 99;

    /** Intent extras key for pet transmission to a Pet Fooding Activity */
    public static final String PET_EXTRA = "petExtra";

    /** Permissions process status */
    private final SingleLiveEvent<Boolean> permissionProcessDone = new SingleLiveEvent<>();

    /** Logging */
    private static final String TAG = "MainActivity";

    private TextView user_name;
    private ImageView user_photo;
    private Toolbar toolbar;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private NavController navController;
    private View headerView;

    private MainActivityViewModel mainActivityViewModel;
    private PetFoodingControl petFoodingControl;
    private MainActivityPermissionsHelper permissionsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        petFoodingControl = (PetFoodingControl) getApplication();
        mainActivityViewModel = new MainActivityViewModel(petFoodingControl);
        contentInitAndSetup();

        // Permissions Management
        permissionProcessDone.observe(this, bool -> {
            if (petFoodingControl.getUserLogged().getValue() == null) {
                Log.i(TAG, "Launching login activity.");
                launchLoginActivity();
            }
        });
        permissionsHelper = new MainActivityPermissionsHelper(this);
        permissionsHelper.checkApplicationPermissions();
    }

    /**
     * Initialize, define and setup the content elements.
     */
    private void contentInitAndSetup() {
        setContentView(R.layout.main_activity);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        setupAddButton();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_pets, R.id.nav_account_settings, R.id.nav_logout)
                .setDrawerLayout(mDrawerLayout)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        headerView = navigationView.getHeaderView(0);
        getUserDataView();
        setupLogoutListener(navigationView);
        setupUserLoggedListener();
    }

    /**
     * Setup the add pet button
     */
    private void setupAddButton() {
        FloatingActionButton addPet = findViewById(R.id.main_addPet);
        addPet.setOnClickListener(view -> sendPetCreationActivityIntent());
    }

    /**
     * Start a Pet Creation activity through an Intent.
     */
    private void sendPetCreationActivityIntent() {
        Intent petCreationActivityIntent = new Intent(this, PetCreationActivity.class);
        startActivity(petCreationActivityIntent);
    }

    /**
     * Get views from the header used to display the User data.
     */
    private void getUserDataView() {
        user_name = headerView.findViewById(R.id.txt_user_name);
        user_photo = headerView.findViewById(R.id.imv_user_photo);
    }

    /**
     * Set the logout button listener to invoke log out on click.
     */
    private void setupLogoutListener(NavigationView navigationView) {
        navigationView.setOnClickListener(view -> {
            if (view == findViewById(R.id.nav_logout)) {
                logout();
            }
        });
    }

    /**
     * Log out.
     */
    public void logout() {
        Log.d(TAG, "logout");
        mainActivityViewModel.logout();
        mDrawerLayout.closeDrawers();
        navController.navigate(R.id.nav_pets);
        navigationView.getMenu().getItem(0).setChecked(true);
        launchLoginActivity();
    }

    /**
     * Launch login activity trough explicit intent.
     */
    public void launchLoginActivity() {
        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(loginActivityIntent, LOGIN_REQUEST);
    }

    /**
     * Setup user logged listener to display its data in the header.
     */
    private void setupUserLoggedListener() {
        petFoodingControl.getUserLogged().observe(this, user -> {
            if (user != null) {
                mainActivityViewModel.updateUserPetsAndPhoto(user);
                setupUserPetsListener();
                setUserDataInNavBar(user);
            }
        });
    }

    /**
     * Setup a listener on user's pet to launch an update on the corresponding ArrayList.
     */
    private void setupUserPetsListener() {
        Observer<List<Pet>> observer = this.mainActivityViewModel::updateUserPetsArrayList;
        mainActivityViewModel.getUserPets().observe(this, observer);
        mainActivityViewModel.getMapUserPetsListener()
                .put(mainActivityViewModel.getUserPets(), observer);
        if (mainActivityViewModel.getUserPets() != null &&
                mainActivityViewModel.getUserPets().getValue() != null ) {
            mainActivityViewModel.updateUserPetsArrayList(
                    mainActivityViewModel.getUserPets().getValue());
        }
    }

    /**
     * Set the User elements (name and photo) in the navigation bar dedicated area according to
     * the User data.
     * @param user : the logged User
     */
    private void setUserDataInNavBar(User user) {
        user_name.setText(user == null ? "********" : user.getDisplayedName());
        mainActivityViewModel.getUserPhoto().observe(this, bitmap -> {
            if (bitmap != null) {
                user_photo.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == MainActivityPermissionsHelper.PERMISSIONS_REQUEST) {
            permissionsHelper.onRequestPermissionsResult(requestCode, permissions,
                    grantResults);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST && resultCode != RESULT_OK) {
            finishAndRemoveTask();
            permissionsHelper.checkApplicationPermissions();
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
        Log.d(TAG, "onDestroy()");
        if (isFinishing()) {
            mainActivityViewModel.finish();
        }
        super.onDestroy();
    }

    @Override
    public void onListFragmentInteraction(Pet pet) {launchPetFoodingActivity(pet);}

    /**
     * Launch a pet fooding acivity for a pet.
     * @param pet the pet to launch a fooding activity for
     */
    private void launchPetFoodingActivity(Pet pet) {
        Intent petFoodingActivityIntent = new Intent(this, PetFoodingActivity.class);
        petFoodingActivityIntent.putExtra(PET_EXTRA, pet);
        startActivity(petFoodingActivityIntent);
    }

    @Override
    public void onDeletePetButtonClick(Pet pet) {
        if (petFoodingControl.getUserLogged().getValue() == null) {return;}
        if (pet.getUserId().equals(petFoodingControl.getUserLogged().getValue().getUserId())) {
            launchPetDeletionDialog(pet);
        } else {
            launchPetFeederStatusDeletionDialog(pet);
        }
    }

    /**
     * Launch a dialog to require a confirmation before effective deletion of a pet.
     * @param pet the pet to delete
     */
    private void launchPetDeletionDialog(Pet pet) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.title_pet_deletion))
                .setMessage(getResources().getString(R.string.pet_deletion_text))
                .setPositiveButton(android.R.string.yes, (dialog, which) ->
                    mainActivityViewModel.deletePet(pet).observe(this, bool -> {
                        if (bool) {
                            showToast(getResources().getString(R.string.pet_deletion_success));
                        } else {
                            showToast(getResources().getString(R.string.pet_deletion_failure));
                        }
                    }))
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    /**
     * Launch a dialog to require a confirmation before effective deletion of a pet.
     * @param pet the pet to delete
     */
    private void launchPetFeederStatusDeletionDialog(Pet pet) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.title_pet_feeder_status_deletion))
                .setMessage(getResources().getString(R.string.pet_feeder_status_deletion_text))
                .setPositiveButton(android.R.string.yes, (dialog, which) ->
                        mainActivityViewModel.removeFeederForPet(pet).observe(this, bool -> {
                            if (bool) {
                                showToast(getResources()
                                        .getString(R.string.pet_feeder_status_deletion_success));
                            } else {
                                showToast(getResources()
                                        .getString(R.string.pet_feeder_status_deletion_failure));
                            }
                        }))
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    public void onSaveButtonClick(Map<UserServiceKeysEnum, String> userData) {
        mainActivityViewModel.updateUser(userData).observe(this, result -> {
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
        mainActivityViewModel.updateUserPhoto(userData).observe(
                this, updateResult -> {
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

    public MainActivityViewModel getMainActivityViewModel() {
        return mainActivityViewModel;
    }

    public SingleLiveEvent<Boolean> getPermissionProcessDone() {
        return permissionProcessDone;
    }
}