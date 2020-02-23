package fr.jbrenier.petfoodingcontrol.android.fragments.main.logout;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import fr.jbrenier.petfoodingcontrol.android.activities.main.MainActivity;
/**
 * A layout-less fragment with the unique function th launch the logout on a navigation on the
 * logout menu item.
 * @author Jérôme Brenier
 */
public class LogoutFragment extends Fragment {

    private MainActivity mainActivity;

    public LogoutFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity.logout();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }
}
