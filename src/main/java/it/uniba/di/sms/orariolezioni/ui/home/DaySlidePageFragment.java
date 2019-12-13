package it.uniba.di.sms.orariolezioni.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Date;

import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.DbHandler;
import it.uniba.di.sms.orariolezioni.data.model.Event;
import it.uniba.di.sms.orariolezioni.data.model.Lesson;
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
        // Retrieve the data passed by
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


        frameLayout.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < adapter.getCount(); i++) {
                    View view = adapter.getView(i, null, frameLayout);
                    frameLayout.addView(view);
                    registerForContextMenu(view);
                }
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
        }else if(getActivity() instanceof TeacherActivity){
            inflater.inflate(R.menu.fragment_day_slide_teacher, menu);
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

        if (item.getItemId() == R.id.remove && selectedView.getTag()=="lesson") {
            db.deleteLesson(selectedView.getId());
            selectedView.setVisibility(View.INVISIBLE);
            selectedView = null;
            return true;
        }else if(item.getItemId() == R.id.ask_change){
            //TODO
        }
        selectedView = null;
        return super.onContextItemSelected(item);
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

