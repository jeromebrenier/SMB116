package fr.jbrenier.petfoodingcontrol.ui.fragments.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.msebera.android.httpclient.client.cache.Resource;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.ui.activities.LoginActivity;

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
        View fragmentView = inflater.inflate(R.layout.login_welcome_fragment, container, false);
        setWelcomeText(fragmentView);
        return fragmentView;
    }

    /**
     * Set the welcome text with the logged User name.
     * @param view of the fragment
     */
    private void setWelcomeText(View view) {
        String display = R.string.welcome +
                ((LoginActivity) getActivity()).getUserLogged().getDisplayedName();
        ((TextView) view.findViewById(R.id.txt_welcome)).setText(display);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



}
