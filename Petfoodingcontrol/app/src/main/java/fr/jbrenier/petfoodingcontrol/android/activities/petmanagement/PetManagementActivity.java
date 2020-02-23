package fr.jbrenier.petfoodingcontrol.android.activities.petmanagement;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import fr.jbrenier.petfoodingcontrol.android.application.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.model.Feeder;
import fr.jbrenier.petfoodingcontrol.android.fragments.petmanagement.SectionsPagerAdapter;
import fr.jbrenier.petfoodingcontrol.android.fragments.petmanagement.feeders.MyPetFeedersRecyclerViewAdapter;
import fr.jbrenier.petfoodingcontrol.android.fragments.petmanagement.feeders.PetFeedersFragment;
import fr.jbrenier.petfoodingcontrol.android.fragments.petmanagement.general.PetGeneralFragment;
import fr.jbrenier.petfoodingcontrol.utils.InputValidatedUtils;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * The super activity for managing a pet (creating a pet or modifying it) within the
 * Pet Fooding Control application.
 * @author Jérôme Brenier
 */
public abstract class PetManagementActivity extends AppCompatActivity
        implements PetFeedersFragment.onRemoveFeederButtonClickListener,
        PetGeneralFragment.OnSaveButtonClickListener {

    /** LOGGING */
    private static final String TAG = "PetManagementActivity";

    PetManagementViewModel petManagementViewModel;
    private View newFeederView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PetFoodingControl petFoodingControl = (PetFoodingControl) getApplication();
        petManagementViewModel = new PetManagementViewModel();
        petFoodingControl.getAppComponent().inject(petManagementViewModel);
        setupContent();
    }

    /**
     * Setup the content of the pet management activity.
     */
    private void setupContent() {
        setContentView(R.layout.pet_management_activity);

        // Needed for title display
        setSupportActionBar(findViewById(R.id.pet_management_toolbar));

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(
                this, this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.addOnPageChangeListener(getOnPageChangeListener(sectionsPagerAdapter));
        TabLayout tabs = findViewById(R.id.pet_management_tabs);
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
            int nbPages = sectionsPagerAdapter.getCount();

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled " + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected" + position);
                for (int pageNumber = 0 ; pageNumber < nbPages ; pageNumber++) {
                    if (pageNumber == position) {
                        ((PetData) sectionsPagerAdapter.getItem(pageNumber))
                                .loadPetData(PetManagementActivity.this);
                    } else {
                        ((PetData) sectionsPagerAdapter.getItem(pageNumber))
                                .savePetData(PetManagementActivity.this);
                    }
                }
                showOrHideNewFeederFloatingButton(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
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
        Button addNewFeederButton = newFeederView.findViewById(R.id.btn_new_feeder_add);
        EditText editFeeder = InputValidatedUtils.getWithValidationControlEmailEditText(
                newFeederView.findViewById(R.id.txt_feeder_email),
                newFeederView.findViewById(R.id.txt_feeder_mail_invalid),
                addNewFeederButton
        );
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
    public void onSaveButtonClick() {}

    public PetManagementViewModel getPetManagementViewModel() {
        return petManagementViewModel;
    }
}