package fr.jbrenier.petfoodingcontrol.ui.fragments.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.ui.activities.login.LoginActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Fragment displaying the customized welcome message.
 * @author Jérôme Brenier
 */
public class LoginWelcomeFragment extends Fragment {

    public static LoginWelcomeFragment newInstance() {
        return new LoginWelcomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_login_welcome, container, false);
        ((LoginActivity) getActivity()).getPetFoodingControlRepository().getUserLogged().observe(
                this, user -> setWelcomeText(fragmentView, user));
        return fragmentView;
    }

    /**
     * Set the welcome text with the logged User name.
     * @param view of the fragment
     */
    private void setWelcomeText(View view, User user) {
        String display = getResources().getString(R.string.welcome) + " " +
                (user == null || user.getDisplayedName() == null ? "" : user.getDisplayedName());
        ((TextView) view.findViewById(R.id.txt_welcome)).setText(display);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        (new Handler()).postDelayed(this::finishLoginActivity, 3000);
    }

    /**
     * Finishes the Login activity.
     */
    private void finishLoginActivity() {
        if (getActivity() != null) {
            ((LoginActivity) getActivity()).finishLoginActivity(RESULT_OK);
        }
    }
}
