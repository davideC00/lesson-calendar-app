package it.uniba.di.sms.orariolezioni.ui.requests;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;

import it.uniba.di.sms.orariolezioni.OrarioLezioniApplication;
import it.uniba.di.sms.orariolezioni.data.DbHandler;
import it.uniba.di.sms.orariolezioni.data.model.Request;

public class RequestsTeacherViewModel extends AndroidViewModel {

    private DbHandler mDb;

    private MutableLiveData<ArrayList<Request>> mRequestOf;

    public RequestsTeacherViewModel(Application application) {
        super(application);
        mRequestOf = new MutableLiveData<>();
        mDb = new DbHandler(application);
        mRequestOf.setValue(mDb.getRequestsOf(((OrarioLezioniApplication) application).getTeacher()));
    }

    public LiveData<ArrayList<Request>> getRequestsOf(){
        return mRequestOf;
    }
}