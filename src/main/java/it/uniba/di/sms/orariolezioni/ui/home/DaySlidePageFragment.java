package it.uniba.di.sms.orariolezioni.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.DbHandler;
import it.uniba.di.sms.orariolezioni.data.model.Lesson;

public class DaySlidePageFragment extends Fragment {

    private Date mDate = new Date();


    public static DaySlidePageFragment newInstance(Date date){
        DaySlidePageFragment f = new DaySlidePageFragment();
        Bundle args = new Bundle();
        args.putLong("date", date.getTime());
        f.setArguments(args);
        return f;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDate.setTime(getArguments().getLong("date"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_day_slide_page, container, false);

        // TODO retrieve data from viewmodel
        // Construct the data source
        DbHandler db = new DbHandler(getContext());
        ArrayList<Lesson> lessons = db.getAllLessonFor(mDate);
        // Create the adapter to convert the array to views
        LessonsAdapter adapter = new LessonsAdapter(getContext(), lessons);
        // Attach the adapter to a ListView
        ListView listView = (ListView) rootView.findViewById(R.id.lvLessons);
        listView.setAdapter(adapter);

        return rootView;
    }
}

