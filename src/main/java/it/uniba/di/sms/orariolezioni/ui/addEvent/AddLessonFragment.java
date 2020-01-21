package it.uniba.di.sms.orariolezioni.ui.addEvent;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import it.uniba.di.sms.orariolezioni.R;
import it.uniba.di.sms.orariolezioni.data.DbHandler;
import it.uniba.di.sms.orariolezioni.data.model.Lesson;

public class AddLessonFragment extends AddEventFragment  {

    private TextView tvTeacherChoose;
    private TextView tvSubjectChoose;

    private DbHandler db;
    private ArrayList<String> teachers;
    private ArrayList<String> subjects;


    public AddLessonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_lesson, container, false);

        tvDate = root.findViewById(R.id.tv_date);
        tvFromTime = root.findViewById(R.id.tvFromTime);
        tvToTime = root.findViewById(R.id.tvToTime);
        View vClose = root.findViewById(R.id.vClose);
        View vSave = root.findViewById(R.id.vSave);
        tvTeacherChoose = root.findViewById(R.id.tvTeacherChoose);
        tvSubjectChoose = root.findViewById(R.id.tvSubjectChoose);

        setTimePicker((View)tvFromTime.getParent(), tvFromTime);
        setTimePicker((View)tvToTime.getParent(), tvToTime);
        setDatePicker((View) tvDate.getParent(), tvDate);

        if(getArguments() != null) {
            mDate = new Date(getArguments().getLong("currentDate"));
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy", getResources().getConfiguration().locale);
            tvDate.setText(formatter.format(mDate));
        }

        // Handle back button in fragment
        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.action_nav_add_lesson_to_nav_home);
                    return true;
                }
                return false;
            }
        });

        db = new DbHandler(getContext());
        teachers = db.getTeachers();
        subjects = db.getTeacherSubjects(teachers.get(0));
        tvTeacherChoose.setText(teachers.get(0));
        tvSubjectChoose.setText(subjects.get(0));

        // Button for saving the fields in database
        vSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( tvTeacherChoose.getText() != null){
                    saveLesson();
                }
            }
        });

        // Close the fragment
        vClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.action_nav_add_lesson_to_nav_home);
                }
            }
        });

        ((View)tvTeacherChoose.getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create Dialog
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                final ArrayAdapter<String> dataAdapter =
                        new ArrayAdapter<String>(getActivity(), R.layout.dialog_dropdown_item, teachers);
                adb.setSingleChoiceItems(dataAdapter, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String teacher = dataAdapter.getItem(which);
                        tvTeacherChoose.setText(teacher);

                        //On change Teacher -> change subjects related
                        subjects = db.getTeacherSubjects(teacher);
                        tvSubjectChoose.setText(subjects.get(0));

                        dialog.dismiss();
                    }

                });
                adb.setNegativeButton("Cancel", null);
                adb.setTitle(getResources().getString(R.string.str_choose_teacher));
                adb.show();

            }
        });

        ((View)tvSubjectChoose.getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create Dialog
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                final ArrayAdapter<String> dataAdapter =
                        new ArrayAdapter<String>(getActivity(), R.layout.dialog_dropdown_item, subjects);
                adb.setSingleChoiceItems(dataAdapter, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvSubjectChoose.setText(dataAdapter.getItem(which));
                        dialog.dismiss();
                    }

                });
                adb.setNegativeButton("Cancel", null);
                adb.setTitle(getResources().getString(R.string.str_choose_teacher));
                adb.show();

            }
        });


        return root;

    }

    private void saveLesson() {
        Calendar c = Calendar.getInstance();
        c.setTime(mDate);
        c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(tvFromTime.getText().toString().split(":")[0]));
        c.set(Calendar.MINUTE, Integer.valueOf(tvFromTime.getText().toString().split(":")[1]));
        fromTime = c.getTime();
        c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(tvToTime.getText().toString().split(":")[0]));
        c.set(Calendar.MINUTE, Integer.valueOf(tvToTime.getText().toString().split(":")[1]));
        toTime = c.getTime();
        Lesson lesson = new Lesson(tvTeacherChoose.getText().toString(),
                tvSubjectChoose.getText().toString(),
                fromTime,
                toTime);

        db.insertLesson(lesson);

        // Navigate to Home
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_nav_add_lesson_to_nav_home);
    }

}
