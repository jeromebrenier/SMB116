package fr.jbrenier.petfoodingcontrol.ui.fragments.accountmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import fr.jbrenier.petfoodingcontrol.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.ui.activities.accountcreation.AccountCreationActivity;
import fr.jbrenier.petfoodingcontrol.ui.activities.main.MainActivity;
import fr.jbrenier.petfoodingcontrol.utils.ImageUtils;

/**
 *
 */
public class AccountManagementFormFragment extends Fragment implements View.OnClickListener {

    /** user data keys */
    public static final String PHOTO_KEY = "photo";
    public static final String USERNAME_KEY = "username";
    public static final String EMAIL_KEY = "email";
    public static final String PASSWORD_KEY = "password";

    /** Image */
    private static final String DUMMY_PHOTO_B64 = "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";
    private boolean isDummyPhoto = true;

    private Activity activity;
    private PetFoodingControlRepository pfcRepository;
    private OnSaveButtonClickListener callback;

    public static AccountManagementFormFragment newInstance() {
        return new AccountManagementFormFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
        // set the parent activity
        activity = getActivity();
    }

    /**
     * Check if the fragment have been attached to a MainActivity (modification mode).
     * If not we are in creation mode.
     *
     * @return true if we are in an account modification mode
     */
    private boolean isAccountModificationMode() {
        return getActivity() != null && !(getActivity() instanceof AccountCreationActivity);
    }

    /**
     * Set the User's data in the inputs of the form. We are here in modification mode.
     */
    private void setUserDataInInput() {
        if (pfcRepository.getUserLogged().getValue() == null) {
            return;
        }
        User user = pfcRepository.getUserLogged().getValue();

        // Image
        if (pfcRepository.getUserPhoto(user) != null) {
            setPhotoInImageView(pfcRepository.getUserPhoto(user).getImage());
            isDummyPhoto = false;
        }

        // text
        ((EditText) activity.findViewById(R.id.txt_account_username))
                .setText(user.getDisplayedName());
        ((EditText) activity.findViewById(R.id.txt_account_email))
                .setText(user.getEmail());
    }

    /**
     * Display the photo in the dedicated ImageView.
     * @param b64Image the image as a base 64 encoded string
     */
    private void setPhotoInImageView(String b64Image) {
        Bitmap bitmap = ImageUtils.getBitmapFromBase64String(b64Image);
        ((ImageView) activity.findViewById(R.id.imv_user_photo)).setImageBitmap(bitmap);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_management_form, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isAccountModificationMode()) {
            pfcRepository = ((MainActivity) activity).getPetFoodingControlRepository();
            setUserDataInInput();
        } else {
            setPhotoInImageView(DUMMY_PHOTO_B64);
        }
        activity.findViewById(R.id.btn_account_save).setOnClickListener(this);
        hideCameraButtonIfNoCameraOrNoPermission();
    }

    /**
     * Hide the camera button if no camera is present on the device, or if the permission to use the
     * one present has not been granted.
     */
    private void hideCameraButtonIfNoCameraOrNoPermission() {
        // check if a camera is present on the device
        if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            setIsCameraPermissionGrantedListener();
        }
    }

    private void setIsCameraPermissionGrantedListener() {
        if (getActivity().getApplication() != null) {
            MutableLiveData<Boolean> isCameraPermissionGranted =
                    ((PetFoodingControl) getActivity().getApplication()).isCameraPermissionGranted;
            isCameraPermissionGranted.observe(this, view -> {
                if (isCameraPermissionGranted.getValue()) {
                    activity.findViewById(R.id.btn_take_user_photo).setVisibility(View.VISIBLE);
                } else {
                    activity.findViewById(R.id.btn_take_user_photo).setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSaveButtonClickListener) {
            callback = (OnSaveButtonClickListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement AccountManagementFormFragment.OnSaveButtonClickListener");
        }
    }

    /**
     * Get the data from inputs and return them in a map.
     * @return the map containing the user's data
     */
    private Map<String, String> getDataFromInput() {
        Map<String, String> data = new HashMap<>();

        if (!isDummyPhoto) {
            Drawable userPhotoDrawable =
                    ((ImageView) activity.findViewById(R.id.imv_user_photo)).getDrawable();
            Bitmap userPhotoBitmap = ((BitmapDrawable) userPhotoDrawable).getBitmap();
            data.put(PHOTO_KEY,
                    ImageUtils.getBase64StringFromBitmap(userPhotoBitmap));
        } else {
            data.put(PHOTO_KEY, null);
        }

        data.put(USERNAME_KEY,
                ((EditText) activity.findViewById(R.id.txt_account_username)).getText().toString());
        data.put(EMAIL_KEY,
                ((EditText) activity.findViewById(R.id.txt_account_email)).getText().toString());
        data.put(PASSWORD_KEY,
                ((EditText) activity.findViewById(R.id.txt_account_password)).getText().toString());
        return data;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_account_save) {
            int validationResult = validateDataFromInput();
            if (validationResult == 0) {
                callback.onSaveButtonClick(getDataFromInput());
            } else {
                showToast(validationResult);
            }
        }
    }

    /**
     * Validate the inputs.
     * @return 0 if valid, 1 if passwords mismatch, 2 if empty input
     */
    private int validateDataFromInput() {
        EditText username = activity.findViewById(R.id.txt_account_username);
        EditText email = activity.findViewById(R.id.txt_account_email);
        EditText password = activity.findViewById(R.id.txt_account_password);
        EditText rpassword = activity.findViewById(R.id.txt_account_retype_password);

        if (username.getText() == null || username.getText().toString().isEmpty() ||
                email.getText() == null || email.getText().toString().isEmpty() ||
                password.getText() == null || password.getText().toString().isEmpty() ||
                rpassword.getText() == null || rpassword.getText().toString().isEmpty()) {
            return 2;
        } else if (!password.getText().toString().equals(rpassword.getText().toString())) {
            return 1;
        }
        return 0;
    }

    /**
     * Show a toast depending of the error type :
     * 1 : passwords and retype password are different
     * 2 : some input is empty
     * @param errorType
     */
    private void showToast(int errorType) {
        Toast toast = Toast.makeText(
                getActivity(),
                errorType == 1 ? R.string.toast_passwords_mismatch : R.string.toast_input_empty,
                Toast.LENGTH_LONG);
        toast.show();
    }

    public void setCallback(OnSaveButtonClickListener callback) {
        this.callback = callback;
    }

    public interface OnSaveButtonClickListener {
        public void onSaveButtonClick(Map<String, String> userData);
    }
}
