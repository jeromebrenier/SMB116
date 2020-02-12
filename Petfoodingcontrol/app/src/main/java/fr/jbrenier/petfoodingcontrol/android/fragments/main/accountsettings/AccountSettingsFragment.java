package fr.jbrenier.petfoodingcontrol.android.fragments.main.accountsettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.android.activities.main.MainActivity;
import fr.jbrenier.petfoodingcontrol.android.fragments.accountmanagement.AccountModificationFormFragment;

/**
 * The fragment dedicated to the User's account settings modifications.
 * @author Jérôme Brenier
 */
public class AccountSettingsFragment extends Fragment {

    private MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.account_settings_fragment, container, false);
        mainActivity = (MainActivity)getActivity();
        // Hide the add a pet button if visible
        if (getActivity().findViewById(R.id.main_addPet).getVisibility() == View.VISIBLE) {
            getActivity().findViewById(R.id.main_addPet).setVisibility(View.INVISIBLE);
        }
        // Toolbar title
        mainActivity.setToolBarTitle(R.string.menu_account_settings);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Fragment accountModificationFormFragment = AccountModificationFormFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.nested_account_management_fragment_frameLayout,
                accountModificationFormFragment).commit();
    }
}