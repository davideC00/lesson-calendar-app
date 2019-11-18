package it.uniba.di.sms.orariolezioni.ui.requests;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.List;

import it.uniba.di.sms.orariolezioni.data.model.Request;


public class RequestsViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private RequestsRepository mRepository;

    private List<Request> mAllRequests;

    public RequestsViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
        //mRepository = new RequestsRepository(application);
        //mAllRequests = mRepository.getAllRequests();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public List<Request> getAllRequests(){
        return (List<Request>) mAllRequests;
    }
}