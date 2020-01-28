package fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Map;

import fr.jbrenier.petfoodingcontrol.services.userservice.UserServiceKeysEnum;
import fr.jbrenier.petfoodingcontrol.ui.activities.petmanagement.PetManagementActivity;
import fr.jbrenier.petfoodingcontrol.ui.activities.petmanagement.PetManagementViewModel;

public class PetManagementFragment extends Fragment {

    /** LOGGING */
    private static final String TAG = "PetManagementFragment";

    protected PetManagementViewModel petManagementViewModel;
    protected PetManagementActivity petManagementActivity;

    private OnSaveButtonClickListener callback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        petManagementActivity = (PetManagementActivity) getActivity();
        if (petManagementActivity != null) {
            petManagementViewModel = petManagementActivity.getPetManagementViewModel();
        } else {
            Log.e(TAG, "Parent activity not available, viewModel not referenced.");
        }
    }


    public void setCallback(OnSaveButtonClickListener callback) {
        this.callback = callback;
    }


    public interface OnSaveButtonClickListener {
        void onSaveButtonClick();
    }
}
