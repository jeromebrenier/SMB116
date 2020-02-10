package fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.general;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Locale;

import fr.jbrenier.petfoodingcontrol.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.photo.Photo;
import fr.jbrenier.petfoodingcontrol.ui.activities.petmanagement.PetCreationActivity;
import fr.jbrenier.petfoodingcontrol.ui.activities.petmanagement.PetData;
import fr.jbrenier.petfoodingcontrol.ui.activities.petmanagement.PetManagementActivity;
import fr.jbrenier.petfoodingcontrol.ui.activities.petmanagement.PetManagementViewModel;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.PetManagementFragment;
import fr.jbrenier.petfoodingcontrol.utils.DateTimeUtils;
import fr.jbrenier.petfoodingcontrol.utils.ImageUtils;
import fr.jbrenier.petfoodingcontrol.utils.InputValidationUtils;

import static android.app.Activity.RESULT_OK;

/**
 * The fragment for pet general information.
 * @author Jérôem Brenier
 */
public class PetGeneralFragment extends PetManagementFragment
        implements PetData, View.OnClickListener {

    /** LOGGING */
    private static final String TAG = "PetGeneralFragment";

    /** REQUESTS */
    private static final int PICK_IMAGE_REQUEST = 10;
    private static final int TAKE_PHOTO_REQUEST = 11;

    private EditText dateEditText;
    private ImageButton datePickerButton;
    private final Calendar calendar = Calendar.getInstance();
    private View petGeneralFragmentView;
    private PetFoodingControl petFoodingControl;
    private PetManagementActivity petManagementActivity;
    private OnSaveButtonClickListener callback;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        petGeneralFragmentView = inflater.inflate(R.layout.pet_general_fragment, container, false);
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



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        petManagementActivity = (PetManagementActivity) getActivity();
        petFoodingControl = (PetFoodingControl) petManagementActivity.getApplication();
        if (petManagementActivity instanceof PetCreationActivity) {
            setDefaultPhoto();
        }
        setupButtonOnClickListeners();
        setupDateEditTextValidation();
        loadPetInfoInInputFromViewModel(petManagementActivity);
        hideAddAFeederButtonIfVisible();
    }

    private void setupButtonOnClickListeners() {
        getActivity().findViewById(R.id.btn_take_pet_photo).setOnClickListener(this);
        getActivity().findViewById(R.id.btn_pick_pet_photo_on_disk).setOnClickListener(this);
        getActivity().findViewById(R.id.btn_pet_save).setOnClickListener(this);
    }

    /**
     * Set the default pet avatar in the ImageView.
     */
    private void setDefaultPhoto() {
        Log.d(TAG, "setDefaultPhoto");
        ((ImageView) petGeneralFragmentView.findViewById(R.id.imv_pet_photo))
                .setImageDrawable(getResources().getDrawable(R.drawable.default_pet, null));
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
    public void onStart() {
        super.onStart();
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
        super.onPause();
    }



    @Override
    public void loadPetData(PetManagementActivity petManagementActivity) {
        loadPetInfoInInputFromViewModel(petManagementActivity);
    }

    /**
     * Load the data from Pet in ViewModel into the input.
     */
    private void loadPetInfoInInputFromViewModel(PetManagementActivity pMA) {
        Log.i(TAG,"loadFoodPetInfoInInputFromViewModel");
        Pet pet;
        Photo photo;
        PetManagementViewModel viewModel = pMA.getPetManagementViewModel();
        if (viewModel == null || viewModel.getPetToAdd() == null) {
            return;
        } else {
            pet = viewModel.getPetToAdd();
        }
        String name = pet.getName();
        if (name != null && !name.equals("")) {
            ((EditText) pMA.findViewById(R.id.txt_pet_name)).setText(name);
        }
        if (pet.getBirthDate() != null) {
            ((EditText) pMA.findViewById(R.id.txt_pet_birthdate))
                    .setText(DateTimeUtils.getStringBirthDateFromOffsetDateTime(pet.getBirthDate()));
        }
        if (viewModel.getPetPhoto() == null) {
            return;
        } else {
            photo = viewModel.getPetPhoto();
        }
        String image = photo.getImage();
        if (image != null && !image.isEmpty()) {
            ((ImageView) pMA.findViewById(R.id.imv_pet_photo))
                    .setImageBitmap(ImageUtils.getBitmapFromBase64String(image));
        }
    }

    @Override
    public void savePetData(PetManagementActivity petManagementActivity) {
        savePetInfoFromInputInViewModel(petManagementActivity);
    }

    /**
     * Save food settings from inputs in the ViewModel.
     */
    private void savePetInfoFromInputInViewModel(PetManagementActivity pMA) {
        Log.i(TAG, "savePetInfoFromInputInViewModel");
        Pet pet;
        Photo photo;
        PetManagementViewModel viewModel = pMA.getPetManagementViewModel();
        PetFoodingControl pFC = (PetFoodingControl) pMA.getApplication();
        if (viewModel == null) {
            return;
        }
        if (viewModel.getPetToAdd() != null) {
            pet = viewModel.getPetToAdd();
        } else {
            pet = new Pet(null, null, null, null, null, null);
        }
        if (pFC.getUserLogged().getValue()
                != null) {
            pet.setUserId(pFC.getUserLogged().
                    getValue().getUserId());
        } else  {
            Log.e(TAG, "No user logged.");
            return;
        }
        String name = ((EditText) pMA.findViewById(R.id.txt_pet_name))
                .getText().toString();
        if (!name.equals("")) {
            pet.setName(name);
        }
        String birthDate = ((EditText) pMA.findViewById(R.id.txt_pet_birthdate))
                .getText().toString();
        if (!birthDate.equals("") && InputValidationUtils.isDateValid(birthDate)) {
            OffsetDateTime date = DateTimeUtils.getOffsetDateTimeFromBirthDate(birthDate);
            pet.setBirthDate(date);
        }
        viewModel.setPetToAdd(pet);
        if (viewModel.getPetPhoto() != null) {
            photo = viewModel.getPetPhoto();
        } else {
            photo = new Photo(null, null);
        }
        Drawable userPhotoDrawable =
                ((ImageView) pMA.findViewById(R.id.imv_pet_photo)).getDrawable();
        Bitmap petPhotoBitmap = ((BitmapDrawable) userPhotoDrawable).getBitmap();
        photo.setImage(ImageUtils.getBase64StringFromBitmap(petPhotoBitmap));
        viewModel.setPetPhoto(photo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pet_save :
                if (checkPetInfos()) {
                    Log.i(TAG, "Saving pet in DB...");
                    savePetInfoFromInputInViewModel((PetManagementActivity) getActivity());
                    callback.onSaveButtonClick();
                }
                break;
            case R.id.btn_pick_pet_photo_on_disk :
                sendPickImageIntent();
                break;
            case R.id.btn_take_pet_photo :
                sendTakePhotoIntent();
                break;
            default :
                break;
        }
    }

    /**
     * Check the data entered for the pet in the general tab (name and birth date).
     * @return true if valid, false otherwise
     */
    private boolean checkPetInfos() {
        String name = ((EditText) petManagementActivity.findViewById(R.id.txt_pet_name))
                .getText().toString();
        String birthDate = ((EditText) petManagementActivity.findViewById(R.id.txt_pet_birthdate))
                .getText().toString();
        if (name.isEmpty()) {
            showToast(R.string.error_empty_pet_name);
            return false;
        }
        if (birthDate.equals("") || !InputValidationUtils.isDateValid(birthDate)) {
            showToast(R.string.error_date_invalid_or_empty);
            return false;
        }
        return true;
    }

    /**
     * Show a toast with the message provided as string resource id.
     * @param resId the string resource id of the message
     */
    private void showToast(int resId) {
        Toast toast = Toast.makeText(getActivity(), resId, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Send an intent to pick an image on the disk of the device.
     */
    private void sendPickImageIntent() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(pickIntent, PICK_IMAGE_REQUEST);
    }

    /**
     * Send an intent to take a photo.
     */
    private void sendTakePhotoIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PHOTO_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == PICK_IMAGE_REQUEST || requestCode == TAKE_PHOTO_REQUEST) {
            final Bundle extras = data.getExtras();
            final Bitmap imageRetrieved = requestCode == PICK_IMAGE_REQUEST
                    ? getBitmapFromIntent(data) : (Bitmap) extras.get("data");
            if (imageRetrieved != null) {
                setPhotoInImageView(ImageUtils.resizePhoto(ImageUtils.cropPhoto(imageRetrieved)));
            }
        }
    }

    /**
     * recover the image form the return intent.
     * @param data the return intent
     * @return the bitmap image, or null if image can't be retrieved
     */
    private Bitmap getBitmapFromIntent(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        if (getActivity().getContentResolver() != null) {
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                Log.i(TAG, "column index : " + columnIndex);
                String picturePath = cursor.getString(columnIndex);
                Log.i(TAG, "picture path : " + picturePath);
                cursor.close();

                return BitmapFactory.decodeFile(picturePath);
            }
        }
        return null;
    }

    /**
     * Display the photo in the dedicated ImageView.
     * @param bitmapImage the bitmap image
     */
    void setPhotoInImageView(Bitmap bitmapImage) {
        ((ImageView) petManagementActivity.findViewById(R.id.imv_pet_photo))
                .setImageBitmap(bitmapImage);
    }

    public void setCallback(OnSaveButtonClickListener callback) {
        this.callback = callback;
    }

    public interface OnSaveButtonClickListener {
        void onSaveButtonClick();
    }
}

