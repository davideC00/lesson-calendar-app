package it.uniba.di.sms.orariolezioni.ui.addEvent;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

        db = new DbHandler(getContext());
        teachers = db.getTeachers();


        if(savedInstanceState != null){ // When configuration has changed restore values of Views
            mDate = new Date(savedInstanceState.getLong("currentDate"));
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy", getResources().getConfiguration().locale);
            tvDate.setText(formatter.format(mDate));
            tvFromTime.setText(savedInstanceState.getString("fromTime"));
            tvToTime.setText(savedInstanceState.getString("toTime"));
            tvTeacherChoose.setText(savedInstanceState.getString("teacherChoose"));
            tvSubjectChoose.setText(savedInstanceState.getString("subjectChoose"));
            subjects = db.getTeacherSubjects(tvTeacherChoose.getText().toString()); //get the subject for the teacher
        } else if(getArguments() != null) { // First creation
            mDate = new Date(getArguments().getLong("currentDate"));
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy", getResources().getConfiguration().locale);
            tvDate.setText(formatter.format(mDate));

            subjects = db.getTeacherSubjects(teachers.get(0)); //get the subject for the first teacher
            tvTeacherChoose.setText(teachers.get(0));
            tvSubjectChoose.setText(subjects.get(0));
        }

        // Handle back button in fragment
        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    navToHome();
                    return true;
                }
                return false;
            }
        });

        // Button for saving the fields in database
        vSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( tvTeacherChoose.getText() != null){
                    saveLesson();
                    navToHome();
                }
            }
        });

        // Close the fragment
        vClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    navToHome();
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
                adb.setNegativeButton(R.string.str_cancel, null);
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
                adb.setTitle(getResources().getString(R.string.str_choose_subject));
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
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong("currentDate", mDate.getTime());
        outState.putString("fromTime", tvFromTime.getText().toString());
        outState.putString("toTime", tvToTime.getText().toString());
        outState.putString("teacherChoose", tvTeacherChoose.getText().toString());
        outState.putString("subjectChoose", tvSubjectChoose.getText().toString());
    }
}
