package fr.jbrenier.petfoodingcontrol.android.fragments.petmanagement;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fr.jbrenier.petfoodingcontrol.android.activities.petmanagement.PetManagementActivity;
import fr.jbrenier.petfoodingcontrol.android.activities.petmanagement.PetManagementViewModel;

/**
 * The pet management fragment.
 * @author Jérôme Brenier
 */
public class PetManagementFragment extends Fragment {

    /** Logging */
    private static final String TAG = "PetManagementFragment";

    // Used by subclasses Protected visibility
    protected PetManagementViewModel petManagementViewModel;
    private PetManagementActivity petManagementActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (petManagementActivity != null) {
            petManagementViewModel = petManagementActivity.getPetManagementViewModel();
        } else {
            Log.e(TAG, "Parent activity not available, viewModel not referenced.");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PetManagementActivity) {
            petManagementActivity = (PetManagementActivity) context;
        }
    }
}
