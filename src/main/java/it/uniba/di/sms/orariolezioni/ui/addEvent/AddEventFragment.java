package it.uniba.di.sms.orariolezioni.ui.addEvent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import it.uniba.di.sms.orariolezioni.R;

public abstract class AddEventFragment extends Fragment {

    protected TextView tvToTime;
    protected TextView tvFromTime;
    protected Date fromTime;
    protected Date toTime;
    protected TextView tvDate;
    protected Date mDate = new Date();

    // Set Date Picker onClick for a View
    protected void setDatePicker(final View v, final TextView tv) {
        v.setOnClickListener(new View.OnClickListener() {
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
    protected void setTimePicker(final View v, final TextView tv){
        Date time;
        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String zeroH = selectedHour <= 9 ? "0" : ""; // Make the string looks 09:00
                        String zeroM = selectedMinute <= 9 ? "0" : ""; // Make the string looks 09:00
                        tv.setText( zeroH + selectedHour + ":" + zeroM + selectedMinute);
                    }
                }, hour, minute, true);

                mTimePicker.show();
            }
        });
    }

    // Convert text of textView into Dates
    protected void setFromToTime(){
        Calendar c = Calendar.getInstance();
        c.setTime(mDate);

        // Convert text of tvFromTIme
        c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(tvFromTime.getText().toString().split(":")[0]));
        c.set(Calendar.MINUTE, Integer.valueOf(tvFromTime.getText().toString().split(":")[1]));
        fromTime = c.getTime();

        // Convert text of tvToTime
        c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(tvToTime.getText().toString().split(":")[0]));
        c.set(Calendar.MINUTE, Integer.valueOf(tvToTime.getText().toString().split(":")[1]));
        toTime = c.getTime();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Don't show action bar
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Restore action bar
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    protected void navToHome(){
        Bundle bundle = new Bundle();
        // pass date to home fragment
        bundle.putLong("date", mDate.getTime());
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_nav_add_lesson_to_nav_home, bundle);
    }
}
