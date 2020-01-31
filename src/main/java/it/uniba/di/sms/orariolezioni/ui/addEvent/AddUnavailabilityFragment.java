package it.uniba.di.sms.orariolezioni.ui.addEvent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import it.uniba.di.sms.orariolezioni.OrarioLezioniApplication;
import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.DbHandler;
import it.uniba.di.sms.orariolezioni.data.model.Unavailability;
import it.uniba.di.sms.orariolezioni.ui.TeacherActivity;

public class AddUnavailabilityFragment extends AddEventFragment {

    private String teacher;
    private DbHandler db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_unavailability, container, false);

        tvDate = root.findViewById(R.id.tv_date);
        tvFromTime = root.findViewById(R.id.tvFromTime);
        tvToTime = root.findViewById(R.id.tvToTime);
        View vClose = root.findViewById(R.id.vClose);
        View vSave = root.findViewById(R.id.vSave);

        setTimePicker((View)tvFromTime.getParent(), tvFromTime);
        setTimePicker((View)tvToTime.getParent(), tvToTime);
        setDatePicker((View)tvDate.getParent(), tvDate);

        if(savedInstanceState != null){ // When configuration has changed restore values of Views
            mDate = new Date(savedInstanceState.getLong("currentDate"));
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy", getResources().getConfiguration().locale);
            tvDate.setText(formatter.format(mDate));
            tvFromTime.setText(savedInstanceState.getString("fromTime"));
            tvToTime.setText(savedInstanceState.getString("toTime"));
        } else if(getArguments() != null) {
            mDate = new Date(getArguments().getLong("currentDate"));
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy", getResources().getConfiguration().locale);
            tvDate.setText(formatter.format(mDate));
        }

        // Set db
        if(getActivity()!=null && getActivity() instanceof TeacherActivity){
            db = new DbHandler(getContext());
            teacher = ((OrarioLezioniApplication) getActivity().getApplication()).getTeacher();
        }

        // Handle back button in fragment
        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    navToHome();
                    return true;
                }
                return false;
            }
        });

        // Button for saving the fields in database
        vSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( teacher!=null){
                    saveUnavailability();
                    navToHome();
                }
            }
        });

        // Close the fragment
        vClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    navToHome();
                }
            }
        });


        return root;

    }

    private void saveUnavailability() {
        setFromToTime();
        Unavailability u = new Unavailability(teacher, fromTime, toTime);
        db.insertUnavailability(u);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong("currentDate", mDate.getTime());
        outState.putString("fromTime", tvFromTime.getText().toString());
        outState.putString("toTime", tvToTime.getText().toString());
    }
}


