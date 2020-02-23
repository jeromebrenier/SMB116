package fr.jbrenier.petfoodingcontrol.android.fragments.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.android.activities.accountcreation.AccountCreationActivity;
import fr.jbrenier.petfoodingcontrol.android.activities.login.LoginActivity;
import fr.jbrenier.petfoodingcontrol.utils.InputValidatedUtils;

/**
 * The fragment that contains the form to complete with credentials in order to login.
 * @author Jérôme Brenier
 */
public class LoginFieldsFragment extends Fragment {

    private View loginFieldsView;
    private LoginActivity loginActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        loginFieldsView = inflater.inflate(R.layout.login_fields_fragment, container, false);
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
            String inputEmail = ((TextView) loginFieldsView
                    .findViewById(R.id.txt_login_email)).getText().toString();
            String inputPassword = ((TextView) loginFieldsView
                    .findViewById(R.id.txt_login_password)).getText().toString();
            if (loginActivity != null) {
                loginActivity.onLoginButtonClick(inputEmail, inputPassword);
            }
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
        if (loginActivity != null) {
            InputValidatedUtils.getWithValidationControlEmailEditText(
                    loginActivity.findViewById(R.id.txt_login_email),
                    loginActivity.findViewById(R.id.txt_login_mail_invalid)
            );
        }
    }
}
