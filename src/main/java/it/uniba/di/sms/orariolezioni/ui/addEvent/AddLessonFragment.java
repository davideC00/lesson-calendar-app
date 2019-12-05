package it.uniba.di.sms.orariolezioni.ui.addEvent;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
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

public class AddLessonFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Date mDate = new Date();

    private EditText etToTime;
    private EditText etFromTime;
    private Date fromTime;
    private Date toTime;
    private TextView tvDate;
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

        // Handle back button in fragment
        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    Bundle bundle = new Bundle();
                    bundle.putLong("currentDate", mDate.getTime());
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.action_nav_add_lesson_to_nav_home, bundle);
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

        // Button for saving the fiels in database
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
                    Bundle bundle = new Bundle();
                    bundle.putLong("currentDate", mDate.getTime());
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.action_nav_add_lesson_to_nav_home, bundle);
                }
            }
        });


        setTimePicker(etFromTime);
        setTimePicker(etToTime);
        setDatePicker(tvDate);

        if(getArguments() != null){
            mDate = new Date(getArguments().getLong("currentDate", 0));
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy", getResources().getConfiguration().locale);
            tvDate.setText(formatter.format(mDate));
        }

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
    }

    private boolean isEditTextFilled(TextView et) {
        if(TextUtils.isEmpty(et.getText().toString())){
            et.setError(getResources().getString(R.string.etTimeError));
            return false;
        }
        return true;
    }


    // Set Date Picker onClick for a TextView
    private void setDatePicker(final TextView tv) {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(mDate);
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker;
                datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
                        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy", getResources().getConfiguration().locale);
                        tv.setText(formatter.format(mDate));
                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });
    }

    // Set Time Picker onClick for a EditText
    public void setTimePicker(final EditText et){
        Date time;
        et.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        et.setText( selectedHour + ":" + selectedMinute);
                        et.setError(null);
                    }
                }, hour, minute, true);

                mTimePicker.show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinTeacher){
            teacher = parent.getSelectedItem().toString();
            ArrayAdapter<String> dataAdapter =
                    new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, db.getTeacherSubjects(teacher));
            spinSubject.setAdapter(dataAdapter);
            subject = spinSubject.getSelectedItem().toString();
        }else if(parent.getId() == R.id.spinSubject){
            subject = parent.getSelectedItem().toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
