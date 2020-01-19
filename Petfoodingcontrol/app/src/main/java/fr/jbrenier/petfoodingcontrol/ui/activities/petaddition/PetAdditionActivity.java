package fr.jbrenier.petfoodingcontrol.ui.activities.petaddition;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petaddition.SectionsPagerAdapter;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petaddition.feeders.PetFeedersFragment;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petaddition.feeders.dummy.DummyContent;

/**
 * The activity for adding a pet with the Pet Fooding Control application.
 * @author Jérôme Brenier
 */
public class PetAdditionActivity extends AppCompatActivity
        implements PetFeedersFragment.OnListFragmentInteractionListener {

    private PetAdditionViewModel petAdditionViewModel;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        petAdditionViewModel = ViewModelProviders.of(this).get(PetAdditionViewModel.class);
        setContentView(R.layout.activity_pet_addition);
        setupToolBar();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        setupAddFeederButton();
    }

    private void setupToolBar() {
        //toolbar = findViewById(R.id.pet_addition_toolbar);
 /*       setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/
    }

    /**
     * Set the correct localized toolbar title.
     */
    public void setToolBarTitle(int stringId) {
        //toolbar.setTitle(getResources().getString(stringId));
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
}