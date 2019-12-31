package fr.jbrenier.petfoodingcontrol.ui.fragments.main.accountsettings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccountSettingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AccountSettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is send fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}