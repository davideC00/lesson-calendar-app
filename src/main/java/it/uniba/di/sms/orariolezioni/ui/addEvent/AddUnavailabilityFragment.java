package it.uniba.di.sms.orariolezioni.ui.addEvent;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
        etFromTime = root.findViewById(R.id.etFromTime);
        etToTime = root.findViewById(R.id.etToTime);
        View vClose = root.findViewById(R.id.vClose);
        Button btnSave = root.findViewById(R.id.btnSave);

        setTimePicker(etFromTime);
        setTimePicker(etToTime);
        setDatePicker(tvDate);

        if(getArguments() != null) {
            mDate = new Date(getArguments().getLong("currentDate"));
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy", getResources().getConfiguration().locale);
            tvDate.setText(formatter.format(mDate));
        }

        // Set db
        if(getActivity()!=null && getActivity()instanceof TeacherActivity){
            db = new DbHandler(getContext());
            teacher = ((TeacherActivity) getActivity()).getTeacher();
        }

        // Handle back button in fragment
        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.action_nav_add_lesson_to_nav_home);
                    return true;
                }
                return false;
            }
        });

        // Button for saving the fields in database
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEditTextFilled(etFromTime) && isEditTextFilled(etToTime) && teacher!=null){
                    saveUnavailability();
                }
            }
        });

        // Close the fragment
        vClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.action_nav_add_lesson_to_nav_home);
                }
            }
        });


        return root;

    }

    private void saveUnavailability() {
        setFromToTime();
        Unavailability u = new Unavailability(teacher, fromTime, toTime);
        db.insertUnavailability(u);

        // Navigate to Home
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_nav_add_lesson_to_nav_home);
    }




}


