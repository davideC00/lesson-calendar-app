package it.uniba.di.sms.orariolezioni.ui.addEvent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.DbHandler;

public abstract class AddEventFragment extends Fragment {

    protected EditText etToTime;
    protected EditText etFromTime;
    protected Date fromTime;
    protected Date toTime;
    protected TextView tvDate;
    protected Date mDate = new Date();

    // Set Date Picker onClick for a TextView
    protected void setDatePicker(final TextView tv) {
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
    protected void setTimePicker(final EditText et){
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

    protected boolean isEditTextFilled(TextView et) {
        if(TextUtils.isEmpty(et.getText().toString())){
            et.setError(getResources().getString(R.string.etTimeError));
            return false;
        }
        return true;
    }

    protected void setFromToTime(){
        Calendar c = Calendar.getInstance();
        c.setTime(mDate);
        c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(etFromTime.getText().toString().split(":")[0]));
        c.set(Calendar.MINUTE, Integer.valueOf(etFromTime.getText().toString().split(":")[1]));
        fromTime = c.getTime();
        c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(etToTime.getText().toString().split(":")[0]));
        c.set(Calendar.MINUTE, Integer.valueOf(etToTime.getText().toString().split(":")[1]));
        toTime = c.getTime();
    }
}
