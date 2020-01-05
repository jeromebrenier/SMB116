package fr.jbrenier.petfoodingcontrol.ui.activities.accountmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import fr.jbrenier.petfoodingcontrol.R;

public class AccountManagementActivity extends AppCompatActivity {

    public enum ManagementType {CREATION, MODIFICATION}

    private ManagementType managementType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);
        managementType = (ManagementType) getIntent().getExtras()
                .get(getResources().getString(R.string.account_activity_management_type));
        setActivityTitle();
    }

    private void setActivityTitle() {
        if (managementType == ManagementType.CREATION) {
            getSupportActionBar().setTitle(R.string.title_account_creation);
        }
        else {
            getSupportActionBar().setTitle(R.string.title_account_modification);
        }
    }
}
