package fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.petfoodsettings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.food.FoodSettings;
import fr.jbrenier.petfoodingcontrol.ui.activities.petmanagement.PetData;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.PetManagementFragment;

/**
 * Fragment to manage food settings for a pet.
 * @author Jérôme Brenier
 */
public class PetFoodSettingsFragment extends PetManagementFragment implements PetData {

    /** LOGGING */
    private static final String TAG = "PetGeneralFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pet_food_settings_fragment, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideAddAFeederButtonIfVisible();
    }

    /**
     * Hide the add a feeder floating button if visible.
     */
    private void hideAddAFeederButtonIfVisible() {
        // Hide the add a feeder button if visible
        if (petManagementActivity.findViewById(R.id.add_a_feeder).getVisibility() == View.VISIBLE) {
            petManagementActivity.findViewById(R.id.add_a_feeder).setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void loadPetData() {
        loadFoodSettingsInInputFromViewModel();
    }

    /**
     * Load food settings from the ViewModel in the corresponding inputs.
     */
    private void loadFoodSettingsInInputFromViewModel() {
        Log.i(TAG,"loadFoodSettingsInInputFromViewModel");
        FoodSettings foodSettings;
        if (petManagementViewModel == null || petManagementViewModel.getFoodSettings() == null) {
            return;
        } else {
            foodSettings = petManagementViewModel.getFoodSettings();
        }
        if (foodSettings.getDailyQuantity() != null) {
            ((EditText) petManagementActivity.findViewById(R.id.txt_daily_food))
                    .setText(String.valueOf(foodSettings.getDailyQuantity()));
        }
        List<Integer> portionsRecorded = petManagementViewModel.getFoodSettings()
                .getPreSetPortionList();
        if (portionsRecorded == null) {return;}
        int[] portionsId = {
                R.id.txt_food_portion1,
                R.id.txt_food_portion2,
                R.id.txt_food_portion3,
                R.id.txt_food_portion4,
                R.id.txt_food_portion5,
                R.id.txt_food_portion6};
        int currentIndex = 0;
        for (Integer portionRecorded : portionsRecorded) {
            ((EditText) petManagementActivity.findViewById(portionsId[currentIndex]))
                    .setText(String.valueOf(portionRecorded));
            currentIndex++;
        }
    }

    @Override
    public void savePetData() {
        saveFoodSettingsFromInputInViewModel();
    }

    /**
     * Save food settings from inputs in the ViewModel.
     */
    private void saveFoodSettingsFromInputInViewModel() {
        Log.i(TAG, "saveFoodSettingsFromInputInViewModel");
        FoodSettings foodSettings;
        if (petManagementViewModel == null) {
            return;
        } else if (petManagementViewModel.getFoodSettings() != null) {
            foodSettings = petManagementViewModel.getFoodSettings();
        } else {
            foodSettings = new FoodSettings(null, new ArrayList<>());
        }
        String dailyFood = ((EditText) petManagementActivity.findViewById(R.id.txt_daily_food))
                .getText().toString();
        if (!dailyFood.equals("")) {
            foodSettings.setDailyQuantity(Integer.valueOf(dailyFood));
        }
        int[] portionsId = {
                R.id.txt_food_portion1,
                R.id.txt_food_portion2,
                R.id.txt_food_portion3,
                R.id.txt_food_portion4,
                R.id.txt_food_portion5,
                R.id.txt_food_portion6};
        for (int portionId : portionsId) {
            String portion = ((EditText) petManagementActivity.findViewById(portionId))
                    .getText().toString();
            if (!portion.equals("")) {
                foodSettings.getPreSetPortionList().add(Integer.valueOf(portion));
            }
        }
        petManagementViewModel.setFoodSettings(foodSettings);
    }
}
