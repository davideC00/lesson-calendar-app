package it.uniba.di.sms.orariolezioni.ui.requests;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;

import it.uniba.di.sms.orariolezioni.data.DbHandler;
import it.uniba.di.sms.orariolezioni.data.model.Request;


public class RequestsSchedulerViewModel extends AndroidViewModel {

    private DbHandler mDb;

    private MutableLiveData<ArrayList<Request>> mAllRequests;

    public RequestsSchedulerViewModel(Application application) {
        super(application);
        mAllRequests = new MutableLiveData<>();
        mDb = new DbHandler(application);
        mAllRequests.setValue(mDb.getAllRequests());
    }

    public LiveData<ArrayList<Request>> getAllRequests(){
        return mAllRequests;
    }
}