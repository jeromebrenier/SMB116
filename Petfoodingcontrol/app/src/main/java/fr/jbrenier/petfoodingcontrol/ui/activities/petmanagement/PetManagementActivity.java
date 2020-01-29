package fr.jbrenier.petfoodingcontrol.ui.activities.petmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.androidextras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;
import fr.jbrenier.petfoodingcontrol.services.petservice.PetService;
import fr.jbrenier.petfoodingcontrol.services.photoservice.PhotoService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;
import fr.jbrenier.petfoodingcontrol.ui.activities.main.MainActivity;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.PetManagementFragment;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.SectionsPagerAdapter;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.feeders.PetFeedersFragment;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.feeders.dummy.DummyContent;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.general.PetGeneralFragment;

/**
 * The activity for adding a pet with the Pet Fooding Control application.
 * @author Jérôme Brenier
 */
public class PetManagementActivity extends AppCompatActivity
        implements PetFeedersFragment.OnListFragmentInteractionListener,
        PetGeneralFragment.OnSaveButtonClickListener {

    /** LOGGING */
    private static final String TAG = "PetManagementActivity";

    @Inject
    UserService userService;

    @Inject
    PhotoService photoService;

    @Inject
    PetService petService;

    private PetManagementViewModel petManagementViewModel;
    private boolean isCreationMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PetFoodingControl petFoodingControl = (PetFoodingControl) getApplication();
        petFoodingControl.getAppComponent().inject(this);
        petManagementViewModel = new PetManagementViewModel();
        if (getCallingActivity() != null &&
                !getCallingActivity().getClassName().equals(MainActivity.class.getName())) {
            // Here we are in modification mode
            Log.i(TAG, "Modification mode.");
        } else {
            isCreationMode = true;
            Log.i(TAG, "Creation mode.");
        }
        setContentView(R.layout.activity_pet_management);
        setupToolBar();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(
                this, this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        loadAndSavePetDataOnPageChange(viewPager, sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        setupAddFeederButton();
    }


    private void loadAndSavePetDataOnPageChange(ViewPager viewPager,
                                                SectionsPagerAdapter sectionsPagerAdapter) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int nbItems = sectionsPagerAdapter.getCount();

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((PetData) sectionsPagerAdapter.getItem(position)).loadPetData();
                for (int i = nbItems - 1; i >= 0 ; i--) {
                    if (i != position) {
                        ((PetData) sectionsPagerAdapter.getItem(i)).savePetData();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.pet_addition_toolbar);
        setSupportActionBar(toolbar);
        // Title

        toolbar.setTitle(getResources().getString(
                isCreationMode ? R.string.title_pet_modifcation_activity :
                        R.string.title_add_a_pet_activity));
    }

    /**
     * Setup the floating button to add a feeder to a pet.
     */
    private void setupAddFeederButton() {
        FloatingActionButton addAFeeder = findViewById(R.id.add_a_feeder);
        addAFeeder.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        );
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onSaveButtonClick() {
        if (isCreationMode) {
            Log.i(TAG, "Saving pet in DB...");
            Photo photoToSave = petManagementViewModel.getPetPhoto();
            if (photoToSave.getPhotoId() == null) {
                photoService.save(this, photoToSave, pet);
            }
            saveNewPet().observe(this, result -> {
                if (result) {
                    savePetFeeders();
                    finishPetManagementActivity(RESULT_OK);
                } else {
                    finishPetManagementActivity(RESULT_CANCELED);
                }
            });
        }
    }

    /**
     * Save in the DB a new pet present in the viewModel.
     */
    private SingleLiveEvent<Long> savePhoto() {
        SingleLiveEvent<Long> photoId = new SingleLiveEvent<>();
        Photo photoToSave = petManagementViewModel.getPetPhoto();
        if (photoToSave != null) {
            if (photoToSave.getPhotoId() == null) {
                photoService.save(this, photoToSave, pet);
            }
            petService.save(this, petManagementViewModel.getPetToAdd()).observe(
                    this, pet -> {
                        if (pet == null) {
                            result.setValue(false);
                            Log.i(TAG, "saveNewPet failure.");
                        } else {
                            petManagementViewModel.getPetToAdd().setPetId(pet.getPetId());
                            result.setValue(true);
                            Log.i(TAG, "saveNewPet " + pet.getPetId() + " sucess.");
                        }
                    });
        } else {
            result.setValue(false);
            Log.d(TAG, "No pet to add to the DB");
        }
        return result;
    }

    /**
     * Save in the DB a new pet present in the viewModel.
     */
    private SingleLiveEvent<Boolean> saveNewPet() {
        SingleLiveEvent<Boolean> result = new SingleLiveEvent<>();
        if (petManagementViewModel.getPetToAdd() != null) {
            addFoodSettingsToPet();
            petService.save(this, petManagementViewModel.getPetToAdd()).observe(
                    this, pet -> {
                        if (pet == null) {
                            result.setValue(false);
                            Log.i(TAG, "saveNewPet failure.");
                        } else {
                            petManagementViewModel.getPetToAdd().setPetId(pet.getPetId());
                            result.setValue(true);
                            Log.i(TAG, "saveNewPet " + pet.getPetId() + " sucess.");
                        }
                    });
        } else {
            result.setValue(false);
            Log.d(TAG, "No pet to add to the DB");
        }
        return result;
    }

    /**
     * Add the food settings to the Pet present in the viewModel
     */
    private void addFoodSettingsToPet() {
        if (petManagementViewModel.getFoodSettings() != null) {
            petManagementViewModel.getPetToAdd().setFoodSettings(
                    petManagementViewModel.getFoodSettings());
        }
    }

    private void savePetFeeders() {
    }

    /**
     * Return to the Main activity, and finishes the Login activity.
     */
    private void finishPetManagementActivity(int resultCode) {
        Intent retIntent = new Intent();
        setResult(resultCode, retIntent);
        finish();
    }


    public PetManagementViewModel getPetManagementViewModel() {
        return petManagementViewModel;
    }

    public boolean isCreationMode() {
        return isCreationMode;
    }

    public UserService getUserService() {
        return userService;
    }

    public PhotoService getPhotoService() {
        return photoService;
    }

    public PetService getPetService() {
        return petService;
    }


}