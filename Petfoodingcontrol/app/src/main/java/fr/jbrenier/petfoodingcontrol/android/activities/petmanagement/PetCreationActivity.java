package fr.jbrenier.petfoodingcontrol.android.activities.petmanagement;

import android.util.Log;

/**
 * The activity to create a new pet within the Pet Fooding Control application. it subclasses the
 * {@link PetManagementActivity} tho handle the specificity of the creation activity.
 * @see PetManagementActivity
 * @author Jérôme Brenier
 */
public class PetCreationActivity extends PetManagementActivity {
    /** Logging */
    private static final String TAG = "PetCreationActivity";

    @Override
    public void onSaveButtonClick() {
            Log.d(TAG, "Saving pet in DB...");
            petManagementViewModel.savePetData().observe(this, bool -> finish());
    }
}
