package fr.jbrenier.petfoodingcontrol.ui.fragments.accountmanagement;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.android.material.switchmaterial.SwitchMaterial;

import fr.jbrenier.petfoodingcontrol.R;

public class AccountModificationFormFragment extends AccountCreationFormFragment {

    /** LOGGING */
    private static final String TAG = "AccountModificationFormFragment";

    private SwitchMaterial switchPwdMod;

    public static AccountModificationFormFragment newInstance() {
        return new AccountModificationFormFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUserDataInInput();
        switchPwdMod = activity.findViewById(R.id.switch_mod_pwd);
        setupPasswordModification();
    }

    /**
     * Set the User's data in the inputs of the form.
     */
    private void setUserDataInInput() {
        userService.getPfcRepository().getUserLogged().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                ((EditText) activity.findViewById(R.id.txt_account_username))
                        .setText(user.getDisplayedName());
                ((EditText) activity.findViewById(R.id.txt_account_email))
                        .setText(user.getEmail());
                Log.i(TAG, "User logged changed to " + user.getUserId().toString());
                // Image
                Log.i(TAG, "User Photo id : " + user.getPhotoId().toString());
                photoService.get(this.getContext(), user).observe(getViewLifecycleOwner(), bitmap -> {
                    if (bitmap != null) {
                        setPhotoInImageView(bitmap);
                        Log.i(TAG, "User image loaded.");
                    }
                });
            } else {
                Log.i(TAG, "User logged changed to null");
            }
        });
    }

    /**
     * Setup the switch to allow the password modification and hide / show the password EditText
     * accordingly.
     */
    private void setupPasswordModification() {
        // hide the password fields
        activity.findViewById(R.id.txt_account_password)
                .setVisibility(View.GONE);
        activity.findViewById(R.id.txt_account_retype_password)
                .setVisibility(View.GONE);
        // setup the switch
        switchPwdMod.setVisibility(View.VISIBLE);
        switchPwdMod.setOnCheckedChangeListener((view, checked) -> {
            activity.findViewById(R.id.txt_account_password)
                    .setVisibility(checked ? View.VISIBLE : View.GONE);
            activity.findViewById(R.id.txt_account_retype_password)
                    .setVisibility(checked ? View.VISIBLE : View.GONE);
        });
    }

    /**
     * Validate the inputs.
     * @return 0 if valid, 1 if passwords mismatch, 2 if email invalid, 3 if empty input
     */
    @Override
    int validateDataFromInput() {
        EditText username = activity.findViewById(R.id.txt_account_username);
        EditText email = activity.findViewById(R.id.txt_account_email);
        EditText password = activity.findViewById(R.id.txt_account_password);
        EditText rpassword = activity.findViewById(R.id.txt_account_retype_password);

        boolean checkPassword = switchPwdMod.isChecked();

        if (username.getText() == null || username.getText().toString().isEmpty() ||
                email.getText() == null || email.getText().toString().isEmpty()) {
            return 3;
        } else if (checkPassword &&
                (password.getText() == null || password.getText().toString().isEmpty() ||
                rpassword.getText() == null || rpassword.getText().toString().isEmpty())) {
            return 3;
        } else if (!isEmailValid(email.getText().toString())) {
            return 2;
        } else if (checkPassword &&
                !password.getText().toString().equals(rpassword.getText().toString())) {
            return 1;
        }
        return 0;
    }
}
