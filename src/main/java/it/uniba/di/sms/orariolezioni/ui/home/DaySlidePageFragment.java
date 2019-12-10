package it.uniba.di.sms.orariolezioni.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.Date;

import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.DbHandler;
import it.uniba.di.sms.orariolezioni.data.model.Lesson;

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
        ArrayList<Lesson> lessons = db.getAllLessonFor(mDate);
        // Create the adapter to convert the array to views
        adapter = new LessonsAdapter(getContext(), lessons);


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
        inflater.inflate(R.menu.fragment_page_lesson_menu, menu);
        selectedView = v;
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Since there are multiple instance of fragment in ViewPager onContextItem must be
        // called only on the fragment with selectedView not null
        if(selectedView == null){
            return false;
        }

        if (item.getItemId() == R.id.remove) {
            db.deleteLesson(selectedView.getId());
            selectedView.setVisibility(View.INVISIBLE);
            selectedView = null;
            return true;
        }
        selectedView = null;
        return super.onContextItemSelected(item);
    }

}

