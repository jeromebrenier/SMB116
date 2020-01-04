package fr.jbrenier.petfoodingcontrol.ui.activities.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Base64;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.jbrenier.petfoodingcontrol.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.ui.activities.login.LoginActivity;
import fr.jbrenier.petfoodingcontrol.ui.activities.petaddition.PetAdditionActivity;
import fr.jbrenier.petfoodingcontrol.ui.fragments.main.pets.PetFragment;

/**
 * Main activity of the Pet Fooding Control application.
 * @author Jérôme Brenier
 */
public class MainActivity extends AppCompatActivity implements PetFragment.OnListFragmentInteractionListener {

    private static final int LOGIN_REQUEST = 1;
    private static final int ADD_PET_REQUEST = 2;

    private MainActivityViewModel mainActivityViewModel;
    private NavigationView navigationView;
    private TextView user_name;
    private TextView user_email;
    private ImageView user_photo;
    private Toolbar toolbar;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupAddButton();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_pets, R.id.nav_account_settings, R.id.nav_account_disconnect)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        getUserDataView();
        ((PetFoodingControl) getApplicationContext()).getUserLogged().observe(this, user -> {
            setUserPets(user);
            setUserDataInNavBar(user);
        });
        if (((PetFoodingControl) getApplicationContext()).getUserLogged().getValue() == null) {
            launchLoginActivity();
        }
    }

    /**
     * Setup the add pet button
     */
    private void setupAddButton() {
        FloatingActionButton addPet = findViewById(R.id.addPet);
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
        View headerView = navigationView.getHeaderView(0);
        user_name = headerView.findViewById(R.id.txt_user_name);
        user_email = headerView.findViewById(R.id.txt_user_email);
        user_photo = headerView.findViewById(R.id.imv_user_photo);
    }

    /**
     * Set the User elements (name, email, photo) in the navigation bar dedicated area according to
     * the User data.
     * @param user : the logged User
     */
    private void setUserDataInNavBar(User user) {
        user_name.setText(user.getDisplayedName());
        user_email.setText(user.getEmail());
        if (user.getPhoto() != null && !user.getPhoto().getImage().isEmpty()) {
            byte[] decodedString = Base64.decode(user.getPhoto().getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,
                    decodedString.length);
            user_photo.setImageBitmap(decodedByte);
        }
    }

    /**
     * Set the User pets (owned and authorized to fed) in the viewmodel.
     * @param user : the logged User
     */
    private void setUserPets(User user) {
        List<Pet> listPets = new ArrayList<>();
        user.getPetOwned().stream().forEach(pet -> listPets.add(pet));
        user.getPetAuthorizedToFed().stream().forEach(pet -> listPets.add(pet));
        mainActivityViewModel.getUserPets().setValue(listPets);
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
    private void launchLoginActivity() {
        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(loginActivityIntent, LOGIN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST && resultCode == RESULT_OK) {
            User user = (User) data.getExtras().get(getResources().getString(R.string.user_logged));
            ((PetFoodingControl) getApplicationContext()).setUserLogged(user);
        }
    }

    /**
     * Set the correct localized toolbar title.
     */
    public void setToolBarTitle(int stringId) {
        toolbar.setTitle(getResources().getString(stringId));
    }

    public MainActivityViewModel getMainActivityViewModel() {
        return mainActivityViewModel;
    }

    @Override
    public void onListFragmentInteraction(Pet pet) {

    }
}