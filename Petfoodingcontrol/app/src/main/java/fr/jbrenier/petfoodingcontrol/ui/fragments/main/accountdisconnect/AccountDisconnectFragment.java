package fr.jbrenier.petfoodingcontrol.ui.fragments.main.accountdisconnect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.ui.activities.main.MainActivity;

public class AccountDisconnectFragment extends Fragment {

    private MainActivity mainActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //View root = inflater.inflate(R.layout.fragment_account_disconnect, container, false);
        mainActivity = (MainActivity)getActivity();
        mainActivity.getUserRepository().setUserLogged(null);
        ((NavigationView) mainActivity.findViewById(R.id.nav_view)).getMenu()
                .getItem(0).setChecked(true);
        mainActivity.launchLoginActivity();
        return null;
    }
}