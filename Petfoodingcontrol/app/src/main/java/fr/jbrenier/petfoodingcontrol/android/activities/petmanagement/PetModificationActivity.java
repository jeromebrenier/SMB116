package fr.jbrenier.petfoodingcontrol.android.activities.petmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import fr.jbrenier.petfoodingcontrol.android.activities.petfooding.PetFoodingActivity;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;

/**
 * The activity to modify pet within the Pet Fooding Control application. it subclasses the
 * {@link PetManagementActivity} tho handle the specificity of the creation activity.
 * @see PetManagementActivity
 * @author Jérôme Brenier
 */
public class PetModificationActivity extends PetManagementActivity {
    /** LOGGING */
    private static final String TAG = "PetModificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pet petExtra = getIntent().getParcelableExtra(PetFoodingActivity.PET_MOD_EXTRA);
        if (petExtra != null) {
            petManagementViewModel.setPetInvolved(petExtra);
            petManagementViewModel.loadPetPhoto(petExtra);
            petManagementViewModel.setFoodSettings(petExtra.getFoodSettings());
            petManagementViewModel.populateFeedersArraylist();
        }
    }

    @Override
    public void onSaveButtonClick() {
        Log.d(TAG, "Saving pet modifications in DB...");
        petManagementViewModel.savePetData().observe(
                this, bool -> finishPetModificationActivity());
    }

    /**
     * Finishes the pet modification activity with a return intent.
     */
    private void finishPetModificationActivity() {
        Intent retIntent = new Intent();
        setResult(RESULT_OK, retIntent);
        finish();
    }
}
