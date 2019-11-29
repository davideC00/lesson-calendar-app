package it.uniba.di.sms.orariolezioni.ui.addEvent;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import it.uniba.di.sms.orariolezioni.R;

public class AddLessonFragment extends Fragment {

    private Date mDate = new Date();


    public AddLessonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_lesson, container, false);
        TextView tvDate = root.findViewById(R.id.tv_date);

        if(getArguments() != null){
            mDate = new Date(getArguments().getLong("date", 0));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ITALY);
            tvDate.setText(formatter.format(mDate));
        }

        return root;

    }



}
