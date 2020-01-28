package fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.general;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Locale;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.ui.activities.petmanagement.PetData;
import fr.jbrenier.petfoodingcontrol.ui.activities.petmanagement.PetManagementActivity;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.PetManagementFragment;
import fr.jbrenier.petfoodingcontrol.utils.DateTimeUtils;
import fr.jbrenier.petfoodingcontrol.utils.InputValidationUtils;

/**
 * The fragment for pet general information.
 * @author Jérôem Brenier
 */
public class PetGeneralFragment extends PetManagementFragment implements PetData {

    /** LOGGING */
    private static final String TAG = "PetGeneralFragment";

    private EditText dateEditText;
    private ImageButton datePickerButton;
    private final Calendar calendar = Calendar.getInstance();
    private View petGeneralFragmentView;
    private PetManagementActivity petManagementActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        petGeneralFragmentView = inflater.inflate(R.layout.fragment_pet_general, container, false);
        dateEditText = petGeneralFragmentView.findViewById(R.id.txt_pet_birthdate);
        datePickerButton = petGeneralFragmentView.findViewById(R.id.ibtn_datepicker);
        setupDatePickerLaunch();
        return petGeneralFragmentView;
    }


    /**
     * Setup the launch a DatePicker on a date EditText click and the management of the date
     * selected.
     */
    private void setupDatePickerLaunch() {
        DatePickerDialog.OnDateSetListener onDateListener =
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateEditText();
                };
        setDatePickerButtonOnClickListener(onDateListener);
    }

    /**
     * Update the date EditText with the date picked with the datePicker.
     */
    private void updateDateEditText() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        dateEditText.setText(sdf.format(calendar.getTime()));
    }

    /**
     * Set a listener on the EditText for date to launch a DatePicker and fill in the date.
     */
    private void setDatePickerButtonOnClickListener(
            DatePickerDialog.OnDateSetListener onDateSetListener) {
        datePickerButton.setOnClickListener(
                view -> new DatePickerDialog(getContext(), onDateSetListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show()
        );
    }

    /**
     * Setup a listener on key pressed in the birth date input to validate that it matches the
     * correct dd/MM/yy format. If not the user is alerted by a red color change on the text.
     */
    private void setupDateEditTextValidation() {
        EditText birthDate = petManagementActivity.findViewById(R.id.txt_pet_birthdate);
        TextWatcher birthDateWatcher = new TextWatcher() {
            boolean ignore = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ignore || s.length() == 0) {return;}
                ignore = true;
                if (!InputValidationUtils.isDateValid(s.toString())) {
                    dateEditText.setError(getResources().getString(R.string.error_date));
                }
                ignore = false;
            }
        };
         birthDate.addTextChangedListener(birthDateWatcher);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        petManagementActivity = (PetManagementActivity) getActivity();
        if (petManagementActivity.isCreationMode()) {
            setDefaultPhoto();
        }
        setupDateEditTextValidation();
        loadPetInfoInInputFromViewModel();
        hideAddAFeederButtonIfVisible();
    }

    /**
     * Set the default pet avatar in the ImageView.
     */
    private void setDefaultPhoto() {
        Log.i(TAG, "setDefaultPhoto");
        ((ImageView) petGeneralFragmentView.findViewById(R.id.imv_pet_photo))
                .setImageDrawable(getResources().getDrawable(R.drawable.default_pet, null));
    }

    /**
     * Hide the add a feeder floating button if visible.
     */
    private void hideAddAFeederButtonIfVisible() {
        if (petManagementActivity.findViewById(R.id.add_a_feeder).getVisibility() == View.VISIBLE) {
            petManagementActivity.findViewById(R.id.add_a_feeder).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPause() {
        savePetInfoFromInputInViewModel();
        super.onPause();
    }



    @Override
    public void loadPetData() {
        loadPetInfoInInputFromViewModel();
    }

    /**
     * Load the data from Pet in ViewModel into the input.
     */
    private void loadPetInfoInInputFromViewModel() {
        Log.i(TAG,"loadFoodPetInfoInInputFromViewModel");
        Pet pet;
        if (petManagementViewModel == null || petManagementViewModel.getPetToAdd() == null) {
            return;
        } else {
            pet = petManagementViewModel.getPetToAdd();
        }
        String name = pet.getName();
        if (name != null && !name.equals("")) {
            ((EditText) petManagementActivity.findViewById(R.id.txt_pet_name)).setText(name);
        }
        if (pet.getBirthDate() != null) {
            ((EditText) petManagementActivity.findViewById(R.id.txt_pet_birthdate))
                    .setText(DateTimeUtils.getStringBirthDateFromOffsetDateTime(pet.getBirthDate()));
        }
    }

    @Override
    public void savePetData() {
        savePetInfoFromInputInViewModel();
    }

    /**
     * Save food settings from inputs in the ViewModel.
     */
    private void savePetInfoFromInputInViewModel() {
        Log.i(TAG, "savePetInfoFromInputInViewModel");
        Pet pet;
        if (petManagementViewModel == null) {
            return;
        } else if (petManagementViewModel.getPetToAdd() != null) {
            pet = petManagementViewModel.getPetToAdd();
        } else {
            pet = new Pet(null, null, null, null, null, null);
        }
        if (petManagementActivity.getUserService().getPfcRepository().getUserLogged().getValue()
                != null) {
            pet.setUserId(petManagementActivity.getUserService().getPfcRepository().getUserLogged().
                    getValue().getUserId());
        } else  {
            Log.e(TAG, "No user logged.");
            return;
        }
        String name = ((EditText) petManagementActivity.findViewById(R.id.txt_pet_name))
                .getText().toString();
        if (!name.equals("")) {
            pet.setName(name);
        }
        String birthDate = ((EditText) petManagementActivity.findViewById(R.id.txt_pet_birthdate))
                .getText().toString();
        if (!birthDate.equals("") && InputValidationUtils.isDateValid(birthDate)) {
            OffsetDateTime date = DateTimeUtils.getOffsetDateTimeFromBirthDate(birthDate);
            pet.setBirthDate(date);
        }
        petManagementViewModel.setPetToAdd(pet);
    }
}
