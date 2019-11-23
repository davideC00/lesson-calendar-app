package it.uniba.di.sms.orariolezioni.ui.home;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.DbHandler;
import it.uniba.di.sms.orariolezioni.data.model.Lesson;

public class DaySlidePageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_day_slide_page, container, false);

        // TODO retrieve data from viewmodel
        Lesson lesson = new Lesson("teacher1", "math",
                (new GregorianCalendar(2012, 12, 3, 15, 30, 0)).getTime(),
                (new GregorianCalendar(2012, 12, 3, 17, 48, 0)).getTime());
        Lesson lesson2 = new Lesson( "teacher2", "science",
                (new GregorianCalendar(2012, 12, 3, 17, 38, 0)).getTime(),
                (new GregorianCalendar(2012, 12, 3, 19, 48, 0)).getTime());


        // Construct the data source
        ArrayList<Lesson> lessons = new ArrayList<>();
        DbHandler db = new DbHandler(getContext());
        lessons = db.getAllLessonFor(new GregorianCalendar(2012, 12, 3).getTime());
        // Create the adapter to convert the array to views
        LessonsAdapter adapter = new LessonsAdapter(getContext(), lessons);
        // Attach the adapter to a ListView
        ListView listView = (ListView) rootView.findViewById(R.id.lvLessons);
        listView.setAdapter(adapter);


        return rootView;
    }
}

