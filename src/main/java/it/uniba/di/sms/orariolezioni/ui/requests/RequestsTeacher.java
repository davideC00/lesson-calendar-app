package it.uniba.di.sms.orariolezioni.ui.requests;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.model.Request;

public class RequestsTeacher extends Fragment {

    private RequestsTeacherViewModel requestsTeacherViewModel;
    private ArrayList<Request> mRequestsOf;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        requestsTeacherViewModel =
                ViewModelProviders.of(this).get(RequestsTeacherViewModel.class);
        View root = inflater.inflate(R.layout.fragment_change_requests, container, false);

        final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        final RecyclerView recyclerView = root.findViewById(R.id.rvRequests);

        requestsTeacherViewModel.getRequestsOf().observe(this, new Observer<ArrayList<Request>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Request> requests) {
                mRequestsOf = requests;
                RequestAdapterTeacher adapter = new RequestAdapterTeacher(getContext(), mRequestsOf, navController);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
            }
        });

        return root;
    }
}

