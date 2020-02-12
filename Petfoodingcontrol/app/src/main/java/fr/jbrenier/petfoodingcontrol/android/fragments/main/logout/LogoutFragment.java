package fr.jbrenier.petfoodingcontrol.android.fragments.main.logout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import fr.jbrenier.petfoodingcontrol.android.activities.main.MainActivity;
/**
 * A layout-less fragment with the unique function th launch the logout on a navigation on the
 * logout menu item.
 * @author Jérôme Brenier
 */
public class LogoutFragment extends Fragment {

    public LogoutFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).logout();
    }
}
