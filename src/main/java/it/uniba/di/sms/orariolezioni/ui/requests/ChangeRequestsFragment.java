package it.uniba.di.sms.orariolezioni.ui.requests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import it.uniba.di.sms.orariolezioni.R;

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
        return root;
    }
}