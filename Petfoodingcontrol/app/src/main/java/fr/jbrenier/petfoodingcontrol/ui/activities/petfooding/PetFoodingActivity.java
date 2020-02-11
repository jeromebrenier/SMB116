package fr.jbrenier.petfoodingcontrol.ui.activities.petfooding;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.tabs.TabLayout;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import fr.jbrenier.petfoodingcontrol.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import fr.jbrenier.petfoodingcontrol.ui.activities.main.MainActivity;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petfooding.SectionsPagerAdapter;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petfooding.food.PetFoodFragment;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petfooding.weight.PetWeightFragment;

import static fr.jbrenier.petfoodingcontrol.BR.petfoodingviewmodel;

public class PetFoodingActivity extends AppCompatActivity
                                implements PetFoodFragment.OnFragmentInteractionListener,
                                           PetWeightFragment.OnFragmentInteractionListener {

    /** Logging */
    private static final String TAG = "PetFoodingActivity";

    private PetFoodingViewModel petFoodingViewModel;

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

        // Needed for title display
        setSupportActionBar(findViewById(R.id.pet_fooding_toolbar));

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

    public PetFoodingViewModel getPetFoodingViewModel() {
        return petFoodingViewModel;
    }
}