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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

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

        tvLessonTeacher.setText(lesson.teacher);


        // TODO delete this
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");


        // the duration(hours) is with 000 more so later there are less round errors
        // the real formula should have been (60*60*1000)
        long durationHours = (lesson.toDate.getTime() - lesson.fromDate.getTime())/(60 * 60);

        //Check if the duration of a lesson is greater than 24h
        if(durationHours > 24000){
            durationHours = 24000;
        }

        ViewGroup.LayoutParams params =  convertView.getLayoutParams();
        params.height = ((parent.getHeight()/24)*(int)durationHours)/1000;

        // Check if the height is less the TextView min
        if(params.height < tvLessonTeacher.getMinHeight()) {
            params.height = tvLessonTeacher.getMinHeight();
        }
        convertView.setLayoutParams(params);

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(lesson.fromDate);
        // the minutes and hours are added three 0s more so later there are less round errors
        int minutes = (calendar.get(Calendar.MINUTE)*1000)/60;
        int hours = calendar.get(Calendar.HOUR_OF_DAY)*1000;

        // Distance beetween prev and actual item in listView
        float distanceY = ((parent.getHeight()/24)*(hours + minutes))/1000;
        if(position != 0){
            //if it's not the first lesson take the previus one and calculate the destance from it
            View prevView = parent.getChildAt(position-1);
            distanceY = distanceY - prevView.getHeight();
            Log.i("qwerty", "distancey " + ((parent.getHeight()/24)*(hours + minutes))/1000
                    + " prevY " + prevView.getY() + " prevHei " + prevView.getHeight());
        }

        convertView.setY(distanceY);

        return convertView;
    }
}
