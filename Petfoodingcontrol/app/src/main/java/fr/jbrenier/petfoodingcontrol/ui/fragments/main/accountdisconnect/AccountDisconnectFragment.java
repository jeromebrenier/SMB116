package fr.jbrenier.petfoodingcontrol.ui.fragments.main.accountdisconnect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.ui.activities.main.MainActivity;

public class AccountDisconnectFragment extends Fragment {

    private AccountDisconnectViewModel accountDisconnectViewModel;
    private MainActivity mainActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountDisconnectViewModel =
                ViewModelProviders.of(this).get(AccountDisconnectViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account_disconnect, container, false);
        mainActivity = (MainActivity)getActivity();
        // Toolbar title
        mainActivity.setToolBarTitle(R.string.menu_account_disconnect);
        final TextView textView = root.findViewById(R.id.text_send);
        accountDisconnectViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}