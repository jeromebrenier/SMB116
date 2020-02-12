package fr.jbrenier.petfoodingcontrol.android.fragments.petmanagement.general;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Locale;

import fr.jbrenier.petfoodingcontrol.android.application.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.photo.Photo;
import fr.jbrenier.petfoodingcontrol.android.activities.petmanagement.PetCreationActivity;
import fr.jbrenier.petfoodingcontrol.android.activities.petmanagement.PetData;
import fr.jbrenier.petfoodingcontrol.android.activities.petmanagement.PetManagementActivity;
import fr.jbrenier.petfoodingcontrol.android.activities.petmanagement.PetManagementViewModel;
import fr.jbrenier.petfoodingcontrol.android.fragments.petmanagement.PetManagementFragment;
import fr.jbrenier.petfoodingcontrol.android.uihelpers.InputValidatedHelper;
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

    private final Calendar calendar = Calendar.getInstance();
    private View petGeneralFragmentView;
    private EditText dateInputEditText;
    private PetManagementActivity petManagementActivity;
    private OnSaveButtonClickListener callback;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        petGeneralFragmentView = inflater.inflate(R.layout.pet_general_fragment, container, false);
        return petGeneralFragmentView;
    }

    /**
     * Setup the launch a DatePicker on a date EditText focus taken.
     */
    private void setupDatePickerLaunch() {
        DatePickerDialog.OnDateSetListener onDateListener =
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateEditText();
                };
        setDatePickerLaunchOnFocusChangeListener(onDateListener);
    }

    /**
     * Update the date EditText with the date picked with the datePicker.
     */
    private void updateDateEditText() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        dateInputEditText.setText(sdf.format(calendar.getTime()));
    }

    /**
     * Set a onFocusChange listener on the EditText for date to launch a DatePicker and fill in the
     * date.
     */
    private void setDatePickerLaunchOnFocusChangeListener(
            DatePickerDialog.OnDateSetListener onDateSetListener) {
        dateInputEditText.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                new DatePickerDialog(petManagementActivity, onDateSetListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        petManagementActivity = (PetManagementActivity) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        if (petManagementActivity instanceof PetCreationActivity) {
            setDefaultPhoto();
        }
        setupButtonOnClickListeners();
        dateInputEditText = InputValidatedHelper.getWithValidationControlDateEditText(
                petManagementActivity.findViewById(R.id.txt_pet_birthdate),
                petManagementActivity.findViewById(R.id.txt_pet_birthdate_invalid)
        );
        setupDatePickerLaunch();
        loadPetInfoInInputFromViewModel(petManagementActivity);
        hideAddAFeederButtonIfVisible();
    }

    private void setupButtonOnClickListeners() {
        petManagementActivity.findViewById(R.id.btn_take_pet_photo).setOnClickListener(this);
        petManagementActivity.findViewById(R.id.btn_pick_pet_photo_on_disk)
                .setOnClickListener(this);
        petManagementActivity.findViewById(R.id.btn_pet_save).setOnClickListener(this);
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
     * Hide the add a feeder floating button if visible.
     */
    private void hideAddAFeederButtonIfVisible() {
        if (petManagementActivity.findViewById(R.id.add_a_feeder).getVisibility() == View.VISIBLE) {
            petManagementActivity.findViewById(R.id.add_a_feeder).setVisibility(View.INVISIBLE);
        }
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
                    .setText(DateTimeUtils.getStringDateFromOffsetDateTime(pet.getBirthDate()));
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
            OffsetDateTime date = DateTimeUtils.getOffsetDateTimeFromDate(birthDate);
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

