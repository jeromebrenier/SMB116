package fr.jbrenier.petfoodingcontrol.android.fragments.accountmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.android.application.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.services.disposablemanagement.DisposableManager;
import fr.jbrenier.petfoodingcontrol.services.disposablemanagement.DisposableOwner;
import fr.jbrenier.petfoodingcontrol.services.photoservice.PhotoService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserServiceKeysEnum;
import fr.jbrenier.petfoodingcontrol.utils.InputValidatedUtils;
import fr.jbrenier.petfoodingcontrol.utils.ImageUtils;
import fr.jbrenier.petfoodingcontrol.utils.InputValidationUtils;

import static android.app.Activity.RESULT_OK;

/**
 * The fragment for creating user accounts.
 * @author Jérôme Brenier
 */
public class AccountCreationFormFragment extends Fragment implements View.OnClickListener,
                                                                     DisposableOwner {

    /** Pick image request code */
    private static final int PICK_IMAGE_REQUEST = 1;
    /** Take photo request code */
    private static final int TAKE_PHOTO_REQUEST = 2;

    /** Logging */
    private static final String TAG = "AccountCreationFormFragment";

    @Inject
    DisposableManager disposableManager;

    @Inject
    UserService userService;

    @Inject
    PhotoService photoService;

    Activity activity;
    private PetFoodingControl petFoodingControl;
    private OnSaveButtonClickListener callback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
        // set the parent activity
        activity = getActivity();
        if (activity != null) {
            petFoodingControl = (PetFoodingControl) activity.getApplication();
            petFoodingControl.getAppComponent().inject(this);
        } else {
            Log.e(TAG, "Activity is null, cannot inject dependencies.");
        }
    }

    /**
     * Display the photo in the dedicated ImageView.
     * @param bitmapImage the bitmap image
     */
    void setPhotoInImageView(Bitmap bitmapImage) {
        ((ImageView) activity.findViewById(R.id.imv_user_photo)).setImageBitmap(bitmapImage);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_management_form_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setPhotoInImageView(BitmapFactory.decodeResource(getResources(),R.drawable.user_avatar));
        activity.findViewById(R.id.btn_account_save).setOnClickListener(this);
        activity.findViewById((R.id.btn_pick_user_photo_on_disk)).setOnClickListener(this);
        activity.findViewById((R.id.btn_take_user_photo)).setOnClickListener(this);
        InputValidatedUtils.getWithValidationControlEmailEditText(
                activity.findViewById(R.id.txt_account_email),
                activity.findViewById(R.id.txt_account_email_invalid)
        );
        hideCameraButtonIfNoCameraOrNoPermission();
        hidePickImageButtonIfNoExtStoragePermission();
    }

    /**
     * Hide the camera button if no camera is present on the device, or if the permission to use the
     * one present has not been granted.
     */
    private void hideCameraButtonIfNoCameraOrNoPermission() {
        // check if a camera is present on the device
        if (getContext() != null && getContext().getPackageManager() != null && getContext()
                .getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            manageIsCameraPermissionGranted();
        } else {
            Log.e(TAG, "Cannot hide camera button if no camera or permission granted.");
        }
    }

    /**
     * Set the button proposing to take a photo visible if the camera permission has been granted,
     * and invisible otherwise.
     */
    private void manageIsCameraPermissionGranted() {
        if (petFoodingControl.isCameraPermissionGranted.getValue() == null) {
            return;
        }
        if (petFoodingControl.isCameraPermissionGranted.getValue()) {
            Log.d(TAG, "set Btn Camera visible");
            setBtnVisibility(R.id.btn_take_user_photo, true);
        } else {
            petFoodingControl.isCameraPermissionGranted
                    .observe(getViewLifecycleOwner(),
                            bool -> setBtnVisibility(R.id.btn_take_user_photo, bool));
        }
    }

    /**
     * Set the button whose if is given in parameter visible or invisible according to the
     * visibility status.
     * @param btnId the button id
     * @param visible the visibility status
     */
    private void setBtnVisibility(int btnId, boolean visible) {
        if (visible) {
            activity.findViewById(btnId).setVisibility(View.VISIBLE);
        } else {
            activity.findViewById(btnId).setVisibility(View.GONE);
        }
    }

    /**
     * Hide the camera button if no camera is present on the device, or if the permission to use the
     * one present has not been granted.
     */
    private void hidePickImageButtonIfNoExtStoragePermission() {
        if (petFoodingControl.isReadExternalStoragePermissionGranted.getValue() == null) {
            return;
        }
        if (petFoodingControl.isReadExternalStoragePermissionGranted.getValue()) {
            Log.d(TAG, "set Btn Disk visible");
            setBtnVisibility(R.id.btn_pick_user_photo_on_disk, true);
        } else {
            petFoodingControl.isReadExternalStoragePermissionGranted
                    .observe(getViewLifecycleOwner(),
                            bool -> {
                                setBtnVisibility(R.id.btn_pick_user_photo_on_disk, bool);
                                Log.d(TAG, "lambda set Btn Disk visible");
                            });
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSaveButtonClickListener) {
            callback = (OnSaveButtonClickListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement AccountCreationFormFragment.OnSaveButtonClickListener");
        }
    }

    /**
     * Get the data from inputs and return them in a map.
     * @return the map containing the user's data
     */
    private Map<UserServiceKeysEnum, String> getDataFromInput() {
        Map<UserServiceKeysEnum, String> data = new HashMap<>();

        Drawable userPhotoDrawable =
                ((ImageView) activity.findViewById(R.id.imv_user_photo)).getDrawable();
        Bitmap userPhotoBitmap = ((BitmapDrawable) userPhotoDrawable).getBitmap();
        data.put(UserServiceKeysEnum.PHOTO_KEY,
                ImageUtils.getBase64StringFromBitmap(userPhotoBitmap));

        data.put(UserServiceKeysEnum.USERNAME_KEY,
                ((EditText) activity.findViewById(R.id.txt_account_username)).getText().toString());
        data.put(UserServiceKeysEnum.EMAIL_KEY,
                ((EditText) activity.findViewById(R.id.txt_account_email)).getText().toString());
        data.put(UserServiceKeysEnum.PASSWORD_KEY,
                ((EditText) activity.findViewById(R.id.txt_account_password)).getText().toString());
        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_account_save :
                int validationResult = validateDataFromInput();
                if (validationResult == 0) {
                    callback.onSaveButtonClick(getDataFromInput());
                } else {
                    showToast(validationResult);
                }
                break;
            case R.id.btn_pick_user_photo_on_disk :
                sendPickImageIntent();
                break;
            case R.id.btn_take_user_photo :
                sendTakePhotoIntent();
                break;
            default :
                break;
        }
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

        if (getActivity() != null && getActivity().getContentResolver() != null) {
            if (selectedImage != null) {
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if (cursor != null) {
                    return getBuitmapFromCursor(cursor, filePathColumn);
                }
            } else {
                Log.e(TAG, "Selected image from intent data null.");
            }
        } else {
            Log.e(TAG, "Impossible to get content resolver, or activity null");
        }
        return null;
    }

    /**
     * Get the bitmap image from the cursor given in parameter.
     * @param cursor the cursor containing the image
     * @param filePathColumn the file path column
     * @return the resulting bitmap image
     */
    private Bitmap getBuitmapFromCursor(Cursor cursor, String[] filePathColumn) {
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        Log.i(TAG, "column index : " + columnIndex);
        String picturePath = cursor.getString(columnIndex);
        Log.i(TAG, "picture path : " + picturePath);
        cursor.close();

        return BitmapFactory.decodeFile(picturePath);
    }

    /**
     * Validate the inputs.
     * @return 0 if valid, 1 if passwords mismatch, 2 if email invalid, 3 if empty input
     */
    int validateDataFromInput() {
        EditText username = activity.findViewById(R.id.txt_account_username);
        EditText email = activity.findViewById(R.id.txt_account_email);
        EditText password = activity.findViewById(R.id.txt_account_password);
        EditText rpassword = activity.findViewById(R.id.txt_account_retype_password);

        if (username.getText() == null || username.getText().toString().isEmpty() ||
                email.getText() == null || email.getText().toString().isEmpty() ||
                password.getText() == null || password.getText().toString().isEmpty() ||
                rpassword.getText() == null || rpassword.getText().toString().isEmpty()) {
            return 3;
        } else if (!InputValidationUtils.isEmailValid(email.getText().toString())) {
            return 2;
        } else if (!password.getText().toString().equals(rpassword.getText().toString())) {
            return 1;
        }
        return 0;
    }

    /**
     * Show a toast depending of the error type :
     * 1 : passwords and retype password are different
     * 2 : email invalid
     * 3 : some input is empty
     * @param errorType 1 : passwords mismatch 2: email invalid 3 : incomplete fields filling
     */
    private void showToast(int errorType) {
        int messageId;
        switch (errorType) {
            case 1 :
                messageId = R.string.toast_passwords_mismatch;
                break;
            case 2 :
                messageId = R.string.toast_email_invalid;
                break;
            case 3 :
                messageId = R.string.toast_input_empty;
                break;
            default :
                return;
        }
        Toast toast = Toast.makeText(getActivity(), messageId, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onDestroy() {
        clearDisposables();
        super.onDestroy();
    }

    public void setCallback(OnSaveButtonClickListener callback) {
        this.callback = callback;
    }

    @Override
    public void clearDisposables() {
        disposableManager.clear(this);
    }

    /**
     * Interface to be implemented by the callback class to act on a save button click.
     */
    public interface OnSaveButtonClickListener {
        void onSaveButtonClick(Map<UserServiceKeysEnum, String> userData);
    }
}
