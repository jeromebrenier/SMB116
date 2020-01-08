package fr.jbrenier.petfoodingcontrol.ui.activities.accountmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.ui.fragments.accountmanagement.AccountManagementFormFragment;

/**
 * Activity for creating and modifying User accounts.
 * @author Jérôme Brenier
 */
public class AccountManagementActivity extends AppCompatActivity {

    private static final String DUMMY_TITLE = " ";

    public enum ManagementType {CREATION, MODIFICATION}

    private ManagementType managementType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);
        managementType = (ManagementType) getIntent().getExtras()
                .get(getResources().getString(R.string.account_activity_management_type));
        setActivityTitle();
        if (savedInstanceState == null) {
            loadFragment(AccountManagementFormFragment.newInstance());
        }
    }

    /**
     * Set the correct title in the toolbar.
     */
    private void setActivityTitle() {
        Toolbar toolbar = findViewById(R.id.account_management_toolbar);
        // workaround to get the setTitle method really work afterward
        toolbar.setTitle(DUMMY_TITLE);
        setSupportActionBar(toolbar);
        if (managementType == ManagementType.CREATION) {
            toolbar.setTitle(getResources().getString(R.string.title_account_creation));
        }
        else {
            toolbar.setTitle(getResources().getString(R.string.title_account_modification));
        }
    }

    /**
     * Charge le fragment passé en paramètre
     * @param fragment à charger
     */
    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.account_management_frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
