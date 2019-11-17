package it.uniba.di.sms.orariolezioni.ui.requests;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.DbHandler;
import it.uniba.di.sms.orariolezioni.data.model.Request;

public class Requests extends Fragment {

    private RequestsViewModel requestsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        requestsViewModel =
                ViewModelProviders.of(this).get(RequestsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_change_requests, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        requestsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //Test cardview
        // TODO connect to the database
        Request request1 = new Request("impedovo", "decarolis", "13:30", "14:00");
        Request request2 = new Request("teacher2", "teacher3", "12:30", "13:00");
        Request request3 = new Request("impedovo", "decarolis", "11:30", "14:00");

        DbHandler db = new DbHandler(getContext());
        db.insertRequest(request1, request2, request3);
        ArrayList<Request> requests = db.getAllRequests();

        // requests = (ArrayList<Request>) requestsViewModel.getAllRequests();

        /* LIST VIEW CODE
        RequestsAdapter adapter = new RequestsAdapter(getContext(), requests);

        ListView listView = (ListView) root.findViewById(R.id.lvRequests);
        listView.setAdapter(adapter);
        */
        RequestsAdapter adapter = new RequestsAdapter(getContext(), requests);
        RecyclerView recyclerView= (RecyclerView)root.findViewById(R.id.rvRequests);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        return root;
    }

}