package it.uniba.di.sms.orariolezioni.ui.requests;

import android.app.Application;

import java.util.List;

import it.uniba.di.sms.orariolezioni.data.dao.RequestDao;
import it.uniba.di.sms.orariolezioni.data.model.Request;

public class RequestsRepository {
    private RequestDao mRequestDao;
    private List<Request> mAllRequests;

    RequestsRepository(Application application) {
        // TODO implementing database here?
    }

    List<Request>  getAllRequests() {
        return mAllRequests;
    }

}
