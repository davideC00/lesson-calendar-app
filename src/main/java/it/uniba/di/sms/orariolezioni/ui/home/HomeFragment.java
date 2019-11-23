package it.uniba.di.sms.orariolezioni.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.DbHandler;
import it.uniba.di.sms.orariolezioni.data.model.Lesson;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // TODO retrieve data from viewmodel
        Lesson lesson = new Lesson("teacher1", "math",
                (new GregorianCalendar(2012, 12, 3, 15, 30, 0)).getTime(),
                (new GregorianCalendar(2012, 12, 3, 17, 48, 0)).getTime());
        Lesson lesson2 = new Lesson( "teacher2", "science",
                (new GregorianCalendar(2012, 12, 3, 17, 38, 0)).getTime(),
                (new GregorianCalendar(2012, 12, 3, 19, 48, 0)).getTime());


        // Construct the data source
        ArrayList<Lesson> lessons = new ArrayList<>();
        //lessons.add(lesson);
        //lessons.add(lesson2);
        DbHandler db = new DbHandler(getContext());
        //db.insertLesson(lesson,lesson2);
        lessons = db.getAllLessonFor(new GregorianCalendar(2012, 12, 3).getTime());
        // Create the adapter to convert the array to views
        LessonsAdapter adapter = new LessonsAdapter(getContext(), lessons);
        // Attach the adapter to a ListView
        ListView listView = (ListView) root.findViewById(R.id.lvLessons);
        listView.setAdapter(adapter);

        return root;
    }
}