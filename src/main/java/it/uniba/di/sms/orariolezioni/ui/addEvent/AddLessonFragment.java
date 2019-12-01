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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class AddLessonFragment extends Fragment {

    private Date mDate = new Date();

    private EditText etToTime;
    private EditText etFromTime;
    private TextView tvDate;


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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEditTextFilled(etFromTime) && isEditTextFilled(etToTime)){
                    //saveLesson();
                }
            }
        });

        vClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigateUp();
                }
            }
        });


        setTimePicker(etFromTime);
        setTimePicker(etToTime);
        setDatePicker(tvDate);

        if(getArguments() != null){
            mDate = new Date(getArguments().getLong("date", 0));
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy", getResources().getConfiguration().locale);
            tvDate.setText(formatter.format(mDate));
        }

        return root;

    }

    private boolean isEditTextFilled(EditText et) {
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
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);
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

}
