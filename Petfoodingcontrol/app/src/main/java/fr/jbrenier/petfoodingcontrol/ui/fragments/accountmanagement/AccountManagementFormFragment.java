package fr.jbrenier.petfoodingcontrol.ui.fragments.accountmanagement;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.jbrenier.petfoodingcontrol.R;

public class AccountManagementFormFragment extends Fragment {

    private AccountManagementFormViewModel mViewModel;

    public static AccountManagementFormFragment newInstance() {
        return new AccountManagementFormFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_management_form, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AccountManagementFormViewModel.class);
        // TODO: Use the ViewModel
    }

}
