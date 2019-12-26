package fr.jbrenier.petfoodingcontrol.ui.fragments.main.accountdisconnect;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccountDisconnectViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AccountDisconnectViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is send fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}