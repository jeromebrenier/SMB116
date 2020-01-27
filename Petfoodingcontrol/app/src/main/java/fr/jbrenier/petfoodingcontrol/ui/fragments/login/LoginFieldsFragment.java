package fr.jbrenier.petfoodingcontrol.ui.fragments.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.ui.activities.accountcreation.AccountCreationActivity;
import fr.jbrenier.petfoodingcontrol.ui.activities.login.LoginActivity;
import fr.jbrenier.petfoodingcontrol.utils.InputValidationUtils;

/**
 * The fragment that contains the form to complete with credentials in order to login.
 * @author Jérôme Brenier
 */
public class LoginFieldsFragment extends Fragment {

    private View loginFieldsView;
    private LoginActivity loginActivity;

    /** INITIAL TEXT COLOR */
    private int initTextColor;
    private int alertTextColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        loginFieldsView = inflater.inflate(R.layout.fragment_login_fields, container, false);
        setLoginButtonOnClickListener();
        setupCreateAccountButton();
        return loginFieldsView;
    }

    /**
     * Set a listener to launch an action on a click on the login button.
     */
    private void setLoginButtonOnClickListener() {
        final Button loginButton = loginFieldsView.findViewById(R.id.btn_login);
        loginButton.setOnClickListener(view -> {
            String inputEmail =
                    ((TextView) loginFieldsView.findViewById(R.id.txt_login_email)).getText().toString();
            String inputPassword =
                    ((TextView) loginFieldsView.findViewById(R.id.txt_login_password)).getText().toString();
            ((LoginActivity) getActivity()).onLoginButtonClick(inputEmail, inputPassword);
        });
    }

    /**
     * Launch an account creation activity.
     */
    private void setupCreateAccountButton() {
        final Button createAccountButton = loginFieldsView.findViewById(R.id.btn_account_creation);
        createAccountButton.setOnClickListener(view -> {
            Intent launchAccountCreationActivity =
                    new Intent(getActivity(), AccountCreationActivity.class);
            startActivity(launchAccountCreationActivity);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginActivity = (LoginActivity) getActivity();
        setupEmailVisualValidation();
    }

    /**
     * Check that the text entered in the email EditText is a valid email. Set a listener on key
     * pressed to color the text accordingly to alert the user.
     */
    private void setupEmailVisualValidation() {
        EditText emailEntered = loginActivity.findViewById(R.id.txt_login_email);
        initTextColor = emailEntered.getCurrentTextColor();
        alertTextColor = ContextCompat.getColorStateList(this.getContext(),
                R.color.colorAccent).getDefaultColor();
        TextWatcher emailWatcher = new TextWatcher() {
            boolean ignore = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ignore) {return;}
                ignore = true;
                if (!InputValidationUtils.isEmailValid(s.toString())) {
                    emailEntered.setTextColor(alertTextColor);
                } else {
                    emailEntered.setTextColor(initTextColor);
                }
                ignore = false;
            }
        };
        emailEntered.addTextChangedListener(emailWatcher);
    }
}
