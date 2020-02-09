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
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.SectionsPagerAdapter;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.feeders.MyPetFeedersRecyclerViewAdapter;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.feeders.PetFeedersFragment;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.general.PetGeneralFragment;
import fr.jbrenier.petfoodingcontrol.utils.InputValidationUtils;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * The super activity for managing a pet (creating a pet or modifying it) within the
 * Pet Fooding Control application.
 * @author Jérôme Brenier
 */
public abstract class PetManagementActivity extends AppCompatActivity
        implements PetFeedersFragment.onRemoveFeederButtonClickListener,
        PetGeneralFragment.OnSaveButtonClickListener{

    /** LOGGING */
    private static final String TAG = "PetManagementActivity";

    PetManagementViewModel petManagementViewModel;
    private View newFeederView;
    private Button addNewFeederButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PetFoodingControl petFoodingControl = (PetFoodingControl) getApplication();
        petManagementViewModel = new PetManagementViewModel();
        petFoodingControl.getAppComponent().inject(petManagementViewModel);
        setContentView(R.layout.pet_management_activity);

        // Needed for title display
        setSupportActionBar(findViewById(R.id.pet_management_toolbar));

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(
                this, this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.addOnPageChangeListener(getOnPageChangeListener(sectionsPagerAdapter));
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        setupAddFeederButton();
    }

    /**
     * Return an OnPageChangeListener for managing data loading / saving of pages and new pet feeder
     * button visibility.
     * @param sectionsPagerAdapter the section page adapter used
     * @return the OnPageChangeListener
     */
    private ViewPager.OnPageChangeListener getOnPageChangeListener(
            SectionsPagerAdapter sectionsPagerAdapter) {
        return new ViewPager.OnPageChangeListener() {
            int nbItems = sectionsPagerAdapter.getCount();

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                loadAndSavePetData(position, nbItems, sectionsPagerAdapter);
                showOrHideNewFeederFloatingButton(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    /**
     * Manage data loading / saving of pages depending on the position of the selected page.
     * @param position the position of the selected page
     * @param nbItems the number of pages
     * @param sectionsPagerAdapter the section page adapter used
     */
    private void loadAndSavePetData(int position, int nbItems,
                                    SectionsPagerAdapter sectionsPagerAdapter) {
        ((PetData) sectionsPagerAdapter.getItem(position)).loadPetData();
        Log.d(TAG, "loadPetData() " + position);
        for (int i = nbItems - 1; i >= 0 ; i--) {
            if (i != position) {
                ((PetData) sectionsPagerAdapter.getItem(i)).savePetData();
                Log.d(TAG, "savePetData() " + i);
            }
        }
    }

    /**
     * Manage the visibility of the new pet feeder floating button depending on the page selected.
     * @param position the page selected
     */
    private void showOrHideNewFeederFloatingButton(int position) {
        if (position == 2) {
            setNewFeederFloatingButtonVisibility(true);
        } else {
            setNewFeederFloatingButtonVisibility(false);
        }
    }

    /**
     * Set the visibility of the new pet feeder floating button.
     * @param visible the status of visibility (true if visible, false otherwise)
     */
    private void setNewFeederFloatingButtonVisibility(boolean visible) {
        findViewById(R.id.add_a_feeder).setVisibility(visible ? VISIBLE : INVISIBLE);
    }

    /**
     * Setup the floating button to add a feeder to a pet.
     */
    private void setupAddFeederButton() {
        FloatingActionButton addAFeeder = findViewById(R.id.add_a_feeder);
        addAFeeder.setOnClickListener(view -> newFeederDialog());
    }

    /**
     * Create an alert dialog with a text input to add a new feeder through his address.
     */
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

    /**
     * Setup the Add / Cancel button of the new feeder dialog.
     * @param newFeederDialog the new feeder dialog hosting the buttons
     */
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
    public void onRemoveFeederButtonClick(Feeder feeder, MyPetFeedersRecyclerViewAdapter adapter) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getResources().getString(R.string.new_feeder_confirm_delete_title));
        alertDialog.setMessage(getResources().getString(R.string.new_feeder_confirm_delete_text));

        alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE,
                getResources().getString(R.string.new_feeder_confirm_delete),
                (dialog, which) -> {
                    petManagementViewModel.removeFeeder(feeder);
                    adapter.notifyDataSetChanged();
                });

        alertDialog.setButton(
                AlertDialog.BUTTON_NEGATIVE,
                getResources().getString(R.string.btn_cancel),
                (dialog, which) -> dialog.cancel());

        alertDialog.show();
    }

    @Override
    public void onSaveButtonClick() {
    }

    /**
     * Return to the Main activity, and finishes the Login activity.
     */
    void finishPetManagementActivity(int resultCode) {
        Intent retIntent = new Intent();
        setResult(resultCode, retIntent);
        finish();
    }


    public PetManagementViewModel getPetManagementViewModel() {
        return petManagementViewModel;
    }
}