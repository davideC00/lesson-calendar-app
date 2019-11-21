package it.uniba.di.sms.orariolezioni.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import it.uniba.di.sms.orariolezioni.R;
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
        Lesson lesson = new Lesson("decarolis",
                (new GregorianCalendar(2012, 12, 3, 13, 30, 0)).getTime(),
                (new GregorianCalendar(2012, 12, 3, 17, 48, 0)).getTime());
        Lesson lesson2 = new Lesson( "ponzioPilato",
                (new GregorianCalendar(2012, 12, 3, 17, 38, 0)).getTime(),
                (new GregorianCalendar(2012, 12, 3, 18, 48, 0)).getTime());

        // Construct the data source
        ArrayList<Lesson> lessons = new ArrayList<Lesson>();
        lessons.add(lesson);
        lessons.add(lesson2);
        // Create the adapter to convert the array to views
        LessonsAdapter adapter = new LessonsAdapter(getContext(), lessons);
        // Attach the adapter to a ListView
        ListView listView = (ListView) root.findViewById(R.id.lvLessons);
        listView.setAdapter(adapter);

        ScrollView scrollView = root.findViewById(R.id.svLessons);
        return root;
    }
}