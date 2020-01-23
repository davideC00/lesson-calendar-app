package it.uniba.di.sms.orariolezioni.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Date;

import it.uniba.di.sms.orariolezioni.OrarioLezioniApplication;
import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.DbHandler;
import it.uniba.di.sms.orariolezioni.data.model.Event;
import it.uniba.di.sms.orariolezioni.data.model.Lesson;
import it.uniba.di.sms.orariolezioni.data.model.Request;
import it.uniba.di.sms.orariolezioni.ui.SchedulerActivity;
import it.uniba.di.sms.orariolezioni.ui.TeacherActivity;

public class DaySlidePageFragment extends Fragment {

    private Date mDate = new Date();

    private DbHandler db;

    private FrameLayout frameLayout;

    private LessonsAdapter adapter;

    private View selectedView;

    public static DaySlidePageFragment newInstance(Date date) {
        DaySlidePageFragment f = new DaySlidePageFragment();
        Bundle args = new Bundle();
        args.putLong("date", date.getTime());
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the data passed by newInstance
        mDate.setTime(getArguments().getLong("date"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_day_slide_page, container, false);

        frameLayout = rootView.findViewById(R.id.fragment_calendar);

        // Construct the data source
        db = new DbHandler(getContext());
        ArrayList<Event> events = new ArrayList<>();
        events.addAll(db.getAllLessonFor(mDate));
        events.addAll(db.getAllUnavailabilityFor(mDate));


        // Create the adapter to convert the array to views
        adapter = new LessonsAdapter(getContext(), events);

        final OnPreDrawFragmentPage parent = (OnPreDrawFragmentPage) getParentFragment();
        frameLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (frameLayout.getViewTreeObserver().isAlive())
                    frameLayout.getViewTreeObserver().removeOnPreDrawListener(this);

                // add the view onPreDraw so  frameLayout has a height and lessons/unavailability can be positioned
                for (int i = 0; i < adapter.getCount(); i++) {
                    View view = adapter.getView(i, null, frameLayout);
                    frameLayout.addView(view);
                    registerForContextMenu(view);
                }

                if(parent != null){
                    // Set visible the correct views
                    parent.onPreDrawFragmentPage();
                }
                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        if(getActivity() instanceof SchedulerActivity){
            inflater.inflate(R.menu.fragment_day_slide_scheduler, menu);
        }else if(getActivity() instanceof TeacherActivity && v.getTag()=="lesson"){
            inflater.inflate(R.menu.fragment_day_slide_teacher, menu);
            menu.removeItem(R.id.remove);
        }else if(getActivity() instanceof TeacherActivity && v.getTag()=="unavailability"){
            inflater.inflate(R.menu.fragment_day_slide_teacher, menu);
            menu.removeItem(R.id.ask_change);
        }
        selectedView = v;
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Since there are multiple instance of fragment in ViewPager onContextItem must be
        // called only on the fragment with selectedView not null
        if(selectedView == null){
            return false;
        }

        switch (item.getItemId()) {
            case R.id.remove:
                if(selectedView.getTag()=="lesson") db.deleteLesson(selectedView.getId());
                else if(selectedView.getTag()=="unavailability") db.deleteUnavailability(selectedView.getId());
                frameLayout.removeView(selectedView);
                selectedView = null;
                return true;
            case R.id.ask_change:
                db.insertRequest(new Request(
                        db.getTeacherForLesson(selectedView.getId()),  // Teacher of the lesson
                        ((OrarioLezioniApplication) getActivity().getApplication()).getTeacher(), // Teacher asking for change
                        selectedView.getId() // id of lesson
                ));
                selectedView = null;
                return true;
            default:
                selectedView = null;
                return super.onContextItemSelected(item);
        }
    }

    public ArrayList<View> getViewsWithTag(String tag){
        ArrayList<View> views = new ArrayList<>();
        for(int i = 0; i < frameLayout.getChildCount(); i++){
            if(frameLayout.getChildAt(i).getTag() == tag){
                views.add(frameLayout.getChildAt(i));
            }
        }
        return views;
    }

}

