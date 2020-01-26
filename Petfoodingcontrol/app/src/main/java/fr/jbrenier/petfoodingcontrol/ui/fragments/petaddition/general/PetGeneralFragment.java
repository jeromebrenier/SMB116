package fr.jbrenier.petfoodingcontrol.ui.fragments.petaddition.general;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.ui.activities.petaddition.PetAdditionActivity;

public class PetGeneralFragment extends Fragment {

    private PetGeneralViewModel mViewModel;
    private Activity activity;
    private EditText dateEditText;
    private final Calendar calendar = Calendar.getInstance();

    public static PetGeneralFragment newInstance() {
        return new PetGeneralFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pet_general_fragment, container, false);
        activity = getActivity();
        setDefaultPhoto(root);
        dateEditText = root.findViewById(R.id.txt_pet_birthdate);
        setupDatePickerLaunch();
        hideAddAFeederButtonIfNecessary();
        return root;
    }

    /**
     * Set the default pet avatar in the ImageView.
     * @param rootView the root view of the fragment
     */
    private void setDefaultPhoto(View rootView) {
        ((ImageView) rootView.findViewById(R.id.imv_pet_photo))
                .setImageDrawable(getResources().getDrawable(R.drawable.default_pet, null));
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
        setDateEditTextListener(onDateListener);
    }

    /**
     * Update the date EditText with the date picked with the datePicker.
     */
    private void updateDateEditText() {
        // TODO localization
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        dateEditText.setText(sdf.format(calendar.getTime()));
    }

    /**
     * Set a listener on the EditText for date to launch a DatePicker and fill in the date.
     */
    private void setDateEditTextListener(DatePickerDialog.OnDateSetListener onDateSetListener) {
        dateEditText.setOnClickListener(
                view -> new DatePickerDialog(getContext(), onDateSetListener, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
        );
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
        mViewModel = new PetGeneralViewModel();
        // TODO: Use the ViewModel
    }
}
