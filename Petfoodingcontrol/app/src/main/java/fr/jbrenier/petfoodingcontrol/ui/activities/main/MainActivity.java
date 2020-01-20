package fr.jbrenier.petfoodingcontrol.ui.activities.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Base64;

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
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.ui.activities.login.LoginActivity;
import fr.jbrenier.petfoodingcontrol.ui.activities.petaddition.PetAdditionActivity;
import fr.jbrenier.petfoodingcontrol.ui.fragments.accountmanagement.AccountManagementFormFragment;
import fr.jbrenier.petfoodingcontrol.ui.fragments.main.pets.PetFragment;
import fr.jbrenier.petfoodingcontrol.utils.CryptographyUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

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

    private SharedPreferences sharedPref;

    @Inject
    PetFoodingControlRepository pfcRepository;

    // Manages disposables
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TextView user_name;
    private TextView user_email;
    private ImageView user_photo;
    private Toolbar toolbar;
    private AppBarConfiguration mAppBarConfiguration;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PetFoodingControl) getApplicationContext()).getRepositoryComponent().inject(this);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        setupAddButton();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
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
        pfcRepository.getUserLogged().observe(this, user -> {
            if (user != null) {
                setUserPets(user);
                setUserDataInNavBar(user);
            }
        });
        if (pfcRepository.getUserLogged().getValue() == null) {
            launchLoginActivity();
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
     * Set the logout button listener
     */
    private void setupLogoutListener() {
        headerView.findViewById(R.id.btn_log_out).setOnClickListener(view -> logout());
    }

    private void logout() {
        pfcRepository.setUserLogged(null);
        loadSharedPref();
        Log.i(TAG, " PREFERENCES : " + sharedPref.getAll().keySet());
        if (sharedPref.contains(getString(R.string.autologin_token_id))) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(getString(R.string.autologin_token_id));
            editor.commit();
        }
        launchLoginActivity();
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
     * Set the User elements (name, email, photo) in the navigation bar dedicated area according to
     * the User data.
     * @param user : the logged User
     */
    private void setUserDataInNavBar(User user) {
        user_name.setText(user.getDisplayedName());
        user_email.setText(user.getEmail());
        Log.i(TAG, "user's photo id : " + user.getPhotoId() + " email " + user.getEmail());
        Disposable disposable = pfcRepository.getUserPhoto(user).subscribe(
                photo -> {
                    Log.i(TAG, "loading the photo...");
                    byte[] decodedString = Base64.decode(photo.getImage(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,
                            decodedString.length);
                    user_photo.setImageBitmap(decodedByte);
                }, throwable ->
                    Log.e(TAG, "user photo not loaded", throwable));
        compositeDisposable.add(disposable);
    }

    /**
     * Set the User pets (owned and authorized to fed) in the viewmodel.
     * @param user : the logged User
     */
    private void setUserPets(User user) {
        pfcRepository.setUserPets(user);
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
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void onListFragmentInteraction(Pet pet) {

    }

    public PetFoodingControlRepository getPetFoodingControlRepository() {
        return pfcRepository;
    }

    @Override
    public void onSaveButtonClick(Map<String, String> userData) {
        // USER
        String hashedUserPassword = CryptographyUtils.hashPassword(
                userData.get(AccountManagementFormFragment.PASSWORD_KEY));
        final User newUser = new User(
                userData.get(AccountManagementFormFragment.USERNAME_KEY),
                userData.get(AccountManagementFormFragment.EMAIL_KEY),
                hashedUserPassword,
                null
        );
        // PHOTO
        String base64photo = userData.get(AccountManagementFormFragment.PHOTO_KEY);
        if (base64photo != null) {
            final Photo userPhoto = new Photo(base64photo);
            Disposable disposable = pfcRepository.save(userPhoto).subscribe(
                    (photoId) -> {
                        Log.i(TAG, "User's photo saved with id " + photoId);
                        newUser.setPhotoId(photoId);
                        updateUser(newUser);
                    },
                    throwable -> {
                        Log.e(TAG, "photo save failure", throwable);
                    });
            compositeDisposable.add(disposable);
            pfcRepository.save(userPhoto);
        }
    }

    /**
     * Update the user in the data source.
     * @param user user to save
     */
    private void updateUser(User user) {
        Disposable disposable = pfcRepository.update(user).subscribe(
                () -> {
                    Log.i(TAG,"User with id : " + user.getUserId() + " updated");
                    showToast(getResources().getString(R.string.toast_account_update_success));
                },
                throwable -> {
                    Log.e(TAG, "user update failure", throwable);
                    showToast(getResources().getString(R.string.toast_account_update_failure));
                });
        compositeDisposable.add(disposable);
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