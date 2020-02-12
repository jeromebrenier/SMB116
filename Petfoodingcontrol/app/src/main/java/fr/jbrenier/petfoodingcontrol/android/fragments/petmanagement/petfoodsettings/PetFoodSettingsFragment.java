package fr.jbrenier.petfoodingcontrol.android.fragments.petmanagement.petfoodsettings;

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
import fr.jbrenier.petfoodingcontrol.android.activities.petmanagement.PetData;
import fr.jbrenier.petfoodingcontrol.android.activities.petmanagement.PetManagementActivity;
import fr.jbrenier.petfoodingcontrol.android.activities.petmanagement.PetManagementViewModel;
import fr.jbrenier.petfoodingcontrol.android.fragments.petmanagement.PetManagementFragment;

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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void loadPetData(PetManagementActivity pMA) {
        loadFoodSettingsInInputFromViewModel(pMA);
    }

    /**
     * Load food settings from the ViewModel in the corresponding inputs.
     */
    private void loadFoodSettingsInInputFromViewModel(PetManagementActivity pMA) {
        Log.d(TAG,"loadFoodSettingsInInputFromViewModel");
        FoodSettings foodSettings;
        PetManagementViewModel pMVM = pMA.getPetManagementViewModel();
        if (pMVM == null || pMVM.getFoodSettings() == null) {
            return;
        } else {
            foodSettings = pMVM.getFoodSettings();
        }
        if (foodSettings.getDailyQuantity() != null && foodSettings.getDailyQuantity() != 0) {
            ((EditText) pMA.findViewById(R.id.txt_daily_food))
                    .setText(String.valueOf(foodSettings.getDailyQuantity()));
        }
        List<Integer> portionsRecorded = pMVM.getFoodSettings()
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
            ((EditText) pMA.findViewById(portionsId[currentIndex]))
                    .setText(String.valueOf(portionRecorded));
            currentIndex++;
        }
    }

    @Override
    public void savePetData(PetManagementActivity pMA) {
        saveFoodSettingsFromInputInViewModel(pMA);
    }

    /**
     * Save food settings from inputs in the ViewModel.
     */
    private void saveFoodSettingsFromInputInViewModel(PetManagementActivity pMA) {
        Log.d(TAG, "saveFoodSettingsFromInputInViewModel");
        PetManagementViewModel pMVM = pMA.getPetManagementViewModel();
        FoodSettings foodSettings;
        if (pMVM == null) {
            return;
        } else if (pMVM.getFoodSettings() != null) {
            foodSettings = pMVM.getFoodSettings();
        } else {
            foodSettings = new FoodSettings(0, new ArrayList<>());
        }
        String dailyFood = ((EditText) pMA.findViewById(R.id.txt_daily_food))
                .getText().toString();
        if (!dailyFood.isEmpty()) {
            foodSettings.setDailyQuantity(Integer.valueOf(dailyFood));
        }
        int[] portionsId = {
                R.id.txt_food_portion1,
                R.id.txt_food_portion2,
                R.id.txt_food_portion3,
                R.id.txt_food_portion4,
                R.id.txt_food_portion5,
                R.id.txt_food_portion6};
        foodSettings.getPreSetPortionList().clear();
        for (int portionId : portionsId) {
            String portion = ((EditText) pMA.findViewById(portionId))
                    .getText().toString();
            if (!portion.isEmpty()) {
                foodSettings.getPreSetPortionList().add(Integer.valueOf(portion));
            }
        }
        pMVM.setFoodSettings(foodSettings);
    }
}
