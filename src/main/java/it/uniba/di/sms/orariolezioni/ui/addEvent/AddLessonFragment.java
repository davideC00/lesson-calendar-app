package it.uniba.di.sms.orariolezioni.ui.addEvent;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.DbHandler;
import it.uniba.di.sms.orariolezioni.data.model.Lesson;
import it.uniba.di.sms.orariolezioni.ui.home.PagerViewModel;

public class AddLessonFragment extends AddEventFragment implements AdapterView.OnItemSelectedListener {

    private String teacher;
    private String subject;
    private Spinner spinTeacher;
    private Spinner spinSubject;

    private DbHandler db;


    public AddLessonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_lesson, container, false);

        tvDate = root.findViewById(R.id.tv_date);
        etFromTime = root.findViewById(R.id.etFromTime);
        etToTime = root.findViewById(R.id.etToTime);
        View vClose = root.findViewById(R.id.vClose);
        Button btnSave = root.findViewById(R.id.btnSave);
        spinTeacher = root.findViewById(R.id.spinTeacher);
        spinSubject = root.findViewById(R.id.spinSubject);

        setTimePicker(etFromTime);
        setTimePicker(etToTime);
        setDatePicker(tvDate);

        if(getArguments() != null) {
            mDate = new Date(getArguments().getLong("currentDate"));
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy", getResources().getConfiguration().locale);
            tvDate.setText(formatter.format(mDate));
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

        // Set spin Data
        if(getActivity()!=null){
            db = new DbHandler(getContext());
            ArrayAdapter<String> dataAdapter =
                    new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, db.getTeachers());
            spinTeacher.setAdapter(dataAdapter);
            spinTeacher.setOnItemSelectedListener(this);
            spinSubject.setOnItemSelectedListener(this);
        }

        // Button for saving the fields in database
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEditTextFilled(etFromTime) && isEditTextFilled(etToTime) && teacher!=null){
                    saveLesson();
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

    private void saveLesson() {
        Calendar c = Calendar.getInstance();
        c.setTime(mDate);
        c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(etFromTime.getText().toString().split(":")[0]));
        c.set(Calendar.MINUTE, Integer.valueOf(etFromTime.getText().toString().split(":")[1]));
        fromTime = c.getTime();
        c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(etToTime.getText().toString().split(":")[0]));
        c.set(Calendar.MINUTE, Integer.valueOf(etToTime.getText().toString().split(":")[1]));
        toTime = c.getTime();
        Lesson lesson = new Lesson(teacher, subject, fromTime, toTime);
        db.insertLesson(lesson);

        // Navigate to Home
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_nav_add_lesson_to_nav_home);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinTeacher){
            teacher = parent.getSelectedItem().toString();
            ArrayAdapter<String> dataAdapter =
                    new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, db.getTeacherSubjects(teacher));
            spinSubject.setAdapter(dataAdapter);
            if(spinSubject.getSelectedItem() != null){
                subject = spinSubject.getSelectedItem().toString();
            }
        }else if(parent.getId() == R.id.spinSubject){
            subject = parent.getSelectedItem().toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
