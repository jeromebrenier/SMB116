package fr.jbrenier.petfoodingcontrol.ui.fragments.main.accountsettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.ui.activities.main.MainActivity;
import fr.jbrenier.petfoodingcontrol.ui.fragments.accountmanagement.AccountManagementFormFragment;

public class AccountSettingsFragment extends Fragment {

    private AccountSettingsViewModel accountSettingsViewModel;
    private MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountSettingsViewModel =
                ViewModelProviders.of(this).get(AccountSettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account_settings, container, false);
        mainActivity = (MainActivity)getActivity();
        // hide the add a pet button if visible
        if (getActivity().findViewById(R.id.main_addPet).getVisibility() == View.VISIBLE) {
            getActivity().findViewById(R.id.main_addPet).setVisibility(View.INVISIBLE);
        }
        // Toolbar title
        mainActivity.setToolBarTitle(R.string.menu_account_settings);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Fragment accountManagementFormFragment = AccountManagementFormFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.nested_account_management_fragment_frameLayout,
                accountManagementFormFragment).commit();
    }
}