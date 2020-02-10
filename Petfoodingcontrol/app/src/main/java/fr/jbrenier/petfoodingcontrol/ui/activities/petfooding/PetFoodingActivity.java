package fr.jbrenier.petfoodingcontrol.ui.activities.petfooding;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.ui.activities.main.MainActivity;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petfooding.SectionsPagerAdapter;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petfooding.food.PetFoodFragment;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petfooding.weight.PetWeightFragment;

public class PetFoodingActivity extends AppCompatActivity
                                implements PetFoodFragment.OnFragmentInteractionListener,
                                           PetWeightFragment.OnFragmentInteractionListener {

    private PetFoodingViewModel petFoodingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        petFoodingViewModel = new PetFoodingViewModel(
                getIntent().getParcelableExtra(MainActivity.PET_EXTRA));
        setContentView(R.layout.pet_fooding_activity);

        // Needed for title display
        setSupportActionBar(findViewById(R.id.pet_management_toolbar));

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.pet_fooding_tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}