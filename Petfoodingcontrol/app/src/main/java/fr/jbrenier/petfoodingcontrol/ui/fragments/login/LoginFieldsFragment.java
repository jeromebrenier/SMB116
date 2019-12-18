package fr.jbrenier.petfoodingcontrol.ui.fragments.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.ui.activities.LoginActivity;

public class LoginFieldsFragment extends Fragment {

    private View loginFieldsView;

    public static LoginFieldsFragment newInstance() {
        return new LoginFieldsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        loginFieldsView = inflater.inflate(R.layout.login_fields_fragment, container, false);
        return loginFieldsView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Action on the validation button click.
     */
    public void onLoginButtonClick() {
        String inputEmail =
                ((TextView) loginFieldsView.findViewById(R.id.txt_email)).getText().toString();
        String inputPassword =
                ((TextView) loginFieldsView.findViewById(R.id.txt_password)).getText().toString();
        if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
            sendToastInputEmpty();
        } else {
            ((LoginActivity) getActivity()).onCredentialsEntered(inputEmail, inputPassword);
        }
    }

    /**
     * Display a toast alerting that at least one of the fields is empty.
     */
    private void sendToastInputEmpty() {
        Toast toast = new Toast(this.getContext());
        toast.setText(R.string.toast_input_empty);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }
}
