package fr.jbrenier.petfoodingcontrol.android.activities.petfooding;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.jbrenier.petfoodingcontrol.android.activities.petmanagement.PetModificationActivity;
import fr.jbrenier.petfoodingcontrol.android.application.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.weight.Weighing;
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

    /** Request code for pet modification */
    public static final int PET_MOD_REQUEST = 77;

    /** Intent extras key for pet transmission to a Pet Modification Activity */
    public static final String PET_MOD_EXTRA = "petModExtra";

    private PetFoodingViewModel petFoodingViewModel;
    private View newWeighingView;
    private View weightHistoryView;

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

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(
                this, getSupportFragmentManager());
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
            sendPetModificationActivityIntent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Start a Pet Modiciation activity through an Intent.
     */
    private void sendPetModificationActivityIntent() {
        Intent petModificationActivityIntent = new Intent(this, PetModificationActivity.class);
        petModificationActivityIntent.putExtra(PET_MOD_EXTRA,
                petFoodingViewModel.getPet().getValue());
        startActivityForResult(petModificationActivityIntent, PET_MOD_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PET_MOD_REQUEST && resultCode == RESULT_OK) {
            petFoodingViewModel.refreshObsData();
        }
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
        setupNewWeighingDialogButtons(newWeighingDialog);
        newWeighingDialog.setView(newWeighingView);
        newWeighingDialog.show();
    }


    /**
     * Setup the Add / Cancel button of the new weighing dialog.
     * @param newWeighingDialog the new weighing dialog hosting the buttons
     */
    private void setupNewWeighingDialogButtons(AlertDialog newWeighingDialog) {
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

    public void createShowHistoryDialog() {
        // weight_history_chart
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        weightHistoryView = layoutInflater.inflate(R.layout.weight_history_dialog, null);
        Toolbar toolbar = weightHistoryView.findViewById(R.id.weight_history_toolbar);
        toolbar.setTitle(R.string.title_dialog_weighing_history);
        final AlertDialog weightHistoryDialog = new AlertDialog.Builder(this).create();
        setupWeightHistoryDialogButtons(weightHistoryDialog);
        weightHistoryDialog.setView(weightHistoryView);
        createWeightHistoryChart(weightHistoryView);
        weightHistoryDialog.show();
    }

    private void createWeightHistoryChart(View weightHistoryView) {
        if (petFoodingViewModel.getPetWeighings().getValue() == null ||
                petFoodingViewModel.getPetWeighings().getValue().size() == 0) {
            showToast(R.string.error_weighings_empty);
        } else {
            LineChart weightHistoryChart =
                    weightHistoryView.findViewById(R.id.weight_history_chart);
            weightHistoryChart.setBackgroundColor(Color.WHITE);
            weightHistoryChart.setDragEnabled(true);
            weightHistoryChart.setScaleEnabled(true);
            weightHistoryChart.setData(new LineData(
                    getWeightHistoryChartDataSets(weightHistoryChart)));
        }
    }

    private ArrayList<ILineDataSet> getWeightHistoryChartDataSets(LineChart weightHostoryChart) {
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        ArrayList<Entry> values = new ArrayList<>();
        List<Weighing> weighingList = petFoodingViewModel.getPetWeighings().getValue();
        for (int entryNumber = weighingList.size() - 1 ; entryNumber >= 0 ; entryNumber--) {
            values.add(
                    new Entry(entryNumber, weighingList.get(entryNumber).getWeightInGrams(),
                            getResources().getDrawable(R.drawable.cross_24dp, null)));
        }
        Collections.sort(values, new EntryXComparator());
        LineDataSet set = new LineDataSet(values, "Weighs");
        set.setDrawIcons(false);

        // draw dashed line
        set.enableDashedLine(10f, 5f, 0f);

        // black lines and points
        set.setColor(Color.BLACK);
        set.setCircleColor(Color.BLACK);

        // line thickness and point size
        set.setLineWidth(1f);
        set.setCircleRadius(3f);

        // draw points as solid circles
        set.setDrawCircleHole(false);

        // customize legend entry
        set.setFormLineWidth(1f);
        set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set.setFormSize(15.f);

        // text size of values
        set.setValueTextSize(9f);

        // draw selection line as dashed
        set.enableDashedHighlightLine(10f, 5f, 0f);

        // set the filled area
        set.setDrawFilled(true);
        set.setFillFormatter((dataSet, dataProvider) -> {
            return weightHostoryChart.getAxisLeft().getAxisMinimum();});

        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
            set.setFillDrawable(drawable);
        } else {
            set.setFillColor(Color.BLACK);
        }
        dataSets.add(set); // add the data sets
        return dataSets;
    }

    private void setupWeightHistoryDialogButtons(AlertDialog weightHistoryDialog) {
        Button weightHistoryCloseButton =
                weightHistoryView.findViewById(R.id.btn_weigh_history_close);
        weightHistoryCloseButton.setOnClickListener(view -> weightHistoryDialog.cancel());
    }

    public PetFoodingViewModel getPetFoodingViewModel() {
        return petFoodingViewModel;
    }
}