package fr.jbrenier.petfoodingcontrol.ui.activities.main;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

import java.util.Map;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PetFoodingControl) getApplicationContext()).getAppComponent().inject(this);
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
        if (userService.getPfcRepository().getUserLogged().getValue() == null) {
            launchLoginActivity();
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