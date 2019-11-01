package it.uniba.di.sms.orariolezioni.ui.requests;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.model.Request;

public class ChangeRequestsFragment extends Fragment {

    private ChangeRequestsViewModel changeRequestsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        changeRequestsViewModel =
                ViewModelProviders.of(this).get(ChangeRequestsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_change_requests, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        changeRequestsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //Test cardview
        // TODO connect to the database
        Request request1 = new Request("impedovo", "decarolis", "13:30", "14:00");
        Request request2 = new Request("teacher2", "teacher3", "12:30", "13:00");
        ArrayList<Request> requests = new ArrayList<>();
        requests.add(request1);
        requests.add(request2);
        RequestsAdapter adapter = new RequestsAdapter(getContext(), requests);

        ListView listView = (ListView) root.findViewById(R.id.lvRequests);
        listView.setAdapter(adapter);

        return root;
    }
}