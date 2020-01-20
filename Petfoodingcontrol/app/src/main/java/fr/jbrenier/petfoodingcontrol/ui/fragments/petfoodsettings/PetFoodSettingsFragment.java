package fr.jbrenier.petfoodingcontrol.ui.fragments.petfoodsettings;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.ui.activities.petaddition.PetAdditionActivity;

public class PetFoodSettingsFragment extends Fragment {

    private PetFoodSettingsViewModel mViewModel;
    private Activity activity;

    public static PetFoodSettingsFragment newInstance() {
        return new PetFoodSettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pet_food_settings_fragment, container, false);
        activity = getActivity();
        hideAddAFeederButtonIfNecessary();
        return root;
    }

    /**
     * Hide the add a feeder floating button if it exists (ie we are in the pet addition activity).
     */
    private void hideAddAFeederButtonIfNecessary() {
        if (activity instanceof PetAdditionActivity) {
            // Hide the add a feeder button if visible
            if (activity.findViewById(R.id.add_a_feeder).getVisibility() == View.VISIBLE) {
                activity.findViewById(R.id.add_a_feeder).setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PetFoodSettingsViewModel.class);
        // TODO: Use the ViewModel
    }

}
