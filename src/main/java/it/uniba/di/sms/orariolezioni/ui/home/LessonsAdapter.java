package it.uniba.di.sms.orariolezioni.ui.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.model.Lesson;

public class LessonsAdapter extends ArrayAdapter<Lesson> {

    public LessonsAdapter(@NonNull Context context, ArrayList<Lesson> lessons) {
        super(context, 0, lessons);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Lesson lesson = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lesson, parent, false);
        }

        TextView tvLessonTeacher = convertView.findViewById(R.id.tvLessonTeacher);
        TextView tvLessonTime = convertView.findViewById(R.id.tvLessonTime);

        tvLessonTeacher.setText(lesson.teacher);

        // the duration(hours) is with 000 more so later there are less round errors
        // the real formula should have been (60*60*1000)
        long durationHours = (lesson.toTime.getTime() - lesson.fromTime.getTime())/(60 * 60);

        //Check if the duration of a lesson is greater than 24h
        if(durationHours > 24000){
            durationHours = 24000;
        }

        ViewGroup.LayoutParams params =  convertView.getLayoutParams();
        params.height = getPositionFromTime(parent.getHeight(), (int)durationHours);


        if(params.height < tvLessonTeacher.getMinHeight()) {
            // Restore minHeight
            params.height = tvLessonTeacher.getMinHeight();
        }else if (params.height > tvLessonTime.getTextSize()*4){
            // There is the space for displaying the time as text
            SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm", Locale.ITALY);
            String time = localDateFormat.format(lesson.fromTime) + "-"
                    + localDateFormat.format(lesson.toTime);
            tvLessonTime.setText(time);
        }

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(lesson.fromTime);
        // the minutes and hours are added three 0s more so later there are less round errors
        int minutes = (calendar.get(Calendar.MINUTE)*1000)/60;
        int hours = calendar.get(Calendar.HOUR_OF_DAY)*1000;

        // Distance beetween prev and actual item in listView
        //float distanceY = ((parent.getHeight()/24)*(hours + minutes))/1000;
        int distanceY = getPositionFromTime(parent.getHeight(), hours + minutes);
        if(position != 0){
            //if it's not the first lesson take the previus one and calculate the correct distanceY
            View prevView = parent.getChildAt(position-1);
            distanceY = distanceY - prevView.getHeight();
        }

        convertView.setY(distanceY);

        return convertView;
    }

    // @param time has 000's more
    private int getPositionFromTime(int totalLength, int time){
        return ((totalLength/24)*time)/1000;
    }
}
