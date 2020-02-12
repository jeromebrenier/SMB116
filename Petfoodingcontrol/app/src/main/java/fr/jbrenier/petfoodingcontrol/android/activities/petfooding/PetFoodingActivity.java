package fr.jbrenier.petfoodingcontrol.android.activities.petfooding;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import fr.jbrenier.petfoodingcontrol.android.application.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.android.uihelpers.InputValidatedHelper;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import fr.jbrenier.petfoodingcontrol.android.activities.main.MainActivity;
import fr.jbrenier.petfoodingcontrol.android.fragments.petfooding.SectionsPagerAdapter;
import fr.jbrenier.petfoodingcontrol.android.fragments.petfooding.food.PetFoodFragment;
import fr.jbrenier.petfoodingcontrol.android.fragments.petfooding.weight.PetWeightFragment;

import static fr.jbrenier.petfoodingcontrol.BR.petfoodingviewmodel;

public class PetFoodingActivity extends AppCompatActivity
                                implements PetFoodFragment.OnFragmentInteractionListener,
                                           PetWeightFragment.OnFragmentInteractionListener {

    /** Logging */
    private static final String TAG = "PetFoodingActivity";

    private PetFoodingViewModel petFoodingViewModel;
    private View newWeighingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        petFoodingViewModel = new PetFoodingViewModel();
        ((PetFoodingControl) getApplication()).getAppComponent().inject(petFoodingViewModel);
        Pet petExtra = getIntent().getParcelableExtra(MainActivity.PET_EXTRA);
        petFoodingViewModel.getPet().setValue(petExtra);
        ViewDataBinding petFoodingBinding =
                DataBindingUtil.setContentView(this, R.layout.pet_fooding_activity);
        petFoodingBinding.setLifecycleOwner(this);
        petFoodingBinding.setVariable(petfoodingviewmodel, petFoodingViewModel);

        // Toolbar title display
        Toolbar petFoodingToolbar = findViewById(R.id.pet_fooding_toolbar);
        petFoodingToolbar.setTitle("");
        setSupportActionBar(petFoodingToolbar);
        petFoodingToolbar.setTitle(petExtra.getName());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.pet_fooding_view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.pet_fooding_tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.petfooding_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.petfooding_pet_settings) {
            // TODO : launch pet settings
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void saveFooding(View view) {
        if (view.getId() == R.id.btn_free_portion) {
            saveFreePortionFooding();
        } else {
            Button buttonClicked = (Button)view;
            String buttonText = buttonClicked.getText().toString();
            User userLogged = ((PetFoodingControl) getApplication()).getUserLogged().getValue();
            if (userLogged != null) {
                petFoodingViewModel.saveFooding(Integer.valueOf(buttonText), userLogged);
            }
        }
    }

    private void saveFreePortionFooding() {
        String freePortionEntered = ((EditText) findViewById(R.id.txt_petfooding_free_portion))
                .getText().toString();
        if (!freePortionEntered.isEmpty()) {
            User userLogged = ((PetFoodingControl) getApplication()).getUserLogged().getValue();
            if (userLogged != null) {
                petFoodingViewModel.saveFooding(Integer.valueOf(freePortionEntered), userLogged);
            }
        }
    }

    /**
     * Create a dialog to add a new weight for the pet.
     */
    public void createWeighingAddDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        newWeighingView = layoutInflater.inflate(R.layout.new_weighing_dialog, null);
        Toolbar toolbar = newWeighingView.findViewById(R.id.new_weighing_toolbar);
        toolbar.setTitle(R.string.title_dialog_new_weighing);
        final AlertDialog newWeighingDialog = new AlertDialog.Builder(this).create();
        setupDialogButtons(newWeighingDialog);
        newWeighingDialog.setView(newWeighingView);
        newWeighingDialog.show();
    }

    /**
     * Setup the Add / Cancel button of the new weighing dialog.
     * @param newWeighingDialog the new weighing dialog hosting the buttons
     */
    private void setupDialogButtons(AlertDialog newWeighingDialog) {
        Button addNewWeighingButton = newWeighingView.findViewById(R.id.btn_new_weighing_add);
        EditText editNewWeighing = newWeighingView.findViewById(R.id.txt_new_weighing);
        addNewWeighingButton.setOnClickListener(view -> {
            String newWeighingEntered = editNewWeighing.getText().toString();
            if (newWeighingEntered.isEmpty()) {
                showToast(R.string.new_weighing_error_empty);
            } else {
                Integer newWeighing = Integer.valueOf(editNewWeighing.getText().toString());
                    Observer<Boolean> obs = result -> {
                        if (result) {
                            showToast(R.string.new_weighing_success);
                            newWeighingDialog.dismiss();
                        } else {
                            showToast(R.string.new_weighing_error_unknown);
                        }
                    };
                petFoodingViewModel.saveNewWeighing(newWeighing).observe(this, obs);
            }
        });

        Button cancelButton = newWeighingView.findViewById(R.id.btn_new_weighing_cancel);
        cancelButton.setOnClickListener(view -> newWeighingDialog.cancel());
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

    public PetFoodingViewModel getPetFoodingViewModel() {
        return petFoodingViewModel;
    }
}