package it.uniba.di.sms.orariolezioni.ui.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        String time = localDateFormat.format(lesson.fromDate)
                + "-" + localDateFormat.format(lesson.toDate);
        tvLessonTime.setText(time);

        return convertView;
    }
}
