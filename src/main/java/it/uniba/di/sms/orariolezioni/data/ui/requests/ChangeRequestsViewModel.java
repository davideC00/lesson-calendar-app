package it.uniba.di.sms.orariolezioni.data.ui.requests;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ChangeRequestsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ChangeRequestsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}