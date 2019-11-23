package it.uniba.di.sms.orariolezioni.ui.addEvent;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import it.uniba.di.sms.orariolezioni.R;

public class AddLessonFragment extends Fragment {


    public AddLessonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_lesson, container, false);

        root.setFocusable(true);
        root.setClickable(true);

        return root;

    }


}
