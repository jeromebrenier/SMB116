package fr.jbrenier.petfoodingcontrol.ui.activities.petmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import fr.jbrenier.petfoodingcontrol.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.model.Feeder;
import fr.jbrenier.petfoodingcontrol.ui.activities.main.MainActivity;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.SectionsPagerAdapter;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.feeders.PetFeedersFragment;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.general.PetGeneralFragment;
import fr.jbrenier.petfoodingcontrol.utils.InputValidationUtils;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * The activity for adding a pet with the Pet Fooding Control application.
 * @author Jérôme Brenier
 */
public class PetManagementActivity extends AppCompatActivity
        implements PetFeedersFragment.OnListFragmentInteractionListener,
        PetGeneralFragment.OnSaveButtonClickListener{

    /** LOGGING */
    private static final String TAG = "PetManagementActivity";

    private PetManagementViewModel petManagementViewModel;
    private boolean isCreationMode = false;

    private View newFeederView;
    private Button addNewFeederButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PetFoodingControl petFoodingControl = (PetFoodingControl) getApplication();
        petManagementViewModel = new PetManagementViewModel();
        petFoodingControl.getAppComponent().inject(petManagementViewModel);
        if (getCallingActivity() != null &&
                !getCallingActivity().getClassName().equals(MainActivity.class.getName())) {
            // Here we are in modification mode
            Log.i(TAG, "Modification mode.");
        } else {
            isCreationMode = true;
            Log.i(TAG, "Creation mode.");
        }
        setContentView(R.layout.pet_management_activity);
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
        addAFeeder.setOnClickListener(view -> newFeederDialog());
    }

    private void newFeederDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        newFeederView = layoutInflater.inflate(R.layout.new_feeder_dialog, null);
        Toolbar toolbar = newFeederView.findViewById(R.id.new_feeder_toolbar);
        toolbar.setTitle(R.string.title_dialog_new_pet);
        final AlertDialog newFeederDialog = new AlertDialog.Builder(this).create();
        setupDialogButtons(newFeederDialog);
        newFeederDialog.setView(newFeederView);
        newFeederDialog.show();
    }

    private void setupDialogButtons(AlertDialog newFeederDialog) {
        addNewFeederButton = newFeederView.findViewById(R.id.btn_new_feeder_add);
        addNewFeederButton.setVisibility(INVISIBLE);
        EditText editFeeder = getFeederEmailEditText();
        addNewFeederButton.setOnClickListener(view -> {
            String feederEmail = editFeeder.getText().toString();
            Observer<Integer> obs = result -> {
                switch (result) {
                    case 0 :
                        showToast(R.string.new_feeder_success);
                        newFeederDialog.dismiss();
                        break;
                    case 1 :
                        showToast(R.string.error_user_mail_entered);
                        break;
                    case 2 :
                        showToast(R.string.error_feeder_mail);
                        break;
                    case 3 :
                        showToast(R.string.error_feeder_mail_already_in_list);
                }
            };
            petManagementViewModel.newFeederAddInarraylist(feederEmail).observe(this, obs);
        });

        Button cancelButton = newFeederView.findViewById(R.id.btn_new_feeder_cancel);
        cancelButton.setOnClickListener(view -> newFeederDialog.cancel());
    }

    private EditText getFeederEmailEditText() {
        EditText editFeeder = newFeederView.findViewById(R.id.txt_feeder_email);
        TextView errorMessage = newFeederView.findViewById(R.id.txt_feeder_mail_invalid);
        errorMessage.setVisibility(INVISIBLE);
        editFeeder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (InputValidationUtils.isEmailValid(s.toString())) {
                        addNewFeederButton.setVisibility(VISIBLE);
                        errorMessage.setVisibility(INVISIBLE);
                    } else {
                        addNewFeederButton.setVisibility(INVISIBLE);
                        errorMessage.setVisibility(VISIBLE);
                    }
                } else {
                    addNewFeederButton.setVisibility(INVISIBLE);
                    errorMessage.setVisibility(INVISIBLE);
                }
            }
        });
        return editFeeder;
    }

    /**
     * Create and show a toast.
     * @param resId the resource to show
     */
    private void showToast(int resId) {
        Toast toast = Toast.makeText(this, getResources().getString(resId),
                Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onListFragmentInteraction(Feeder feeder) {

    }

    @Override
    public void onSaveButtonClick() {
        if (isCreationMode) {
            Log.d(TAG, "Saving pet in DB...");
            petManagementViewModel.savePetData().observe(this, bool -> {
                if (bool) {
                    finishPetManagementActivity(RESULT_OK);
                } else {
                    finishPetManagementActivity(RESULT_CANCELED);
                }
            });
        }
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
}