package it.uniba.di.sms.orariolezioni.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.security.auth.Subject;

import it.uniba.di.sms.orariolezioni.data.model.Lesson;
import it.uniba.di.sms.orariolezioni.data.model.User;
import it.uniba.di.sms.orariolezioni.data.model.Request;

import static it.uniba.di.sms.orariolezioni.data.DbContract.*;


public class DbHandler extends SQLiteOpenHelper {



    public DbHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LessonContract.CREATE_TABLE);
        db.execSQL(RequestContract.CREATE_TABLE);
        db.execSQL(UserContract.CREATE_TABLE);
        db.execSQL(UnavailabilityContract.CREATE_TABLE);
        db.execSQL(SubjectContract.CREATE_TABLE);
    }


    public void populateDatabase() {

        Lesson lesson = new Lesson("teacher1", "math",
                (new GregorianCalendar(2019, 11, 2, 15, 30, 0)).getTime(),
                (new GregorianCalendar(2019, 11, 2, 17, 48, 0)).getTime());
        Lesson lesson2 = new Lesson( "teacher2", "science",
                (new GregorianCalendar(2019, 11, 1, 8, 30, 0)).getTime(),
                (new GregorianCalendar(2019, 11, 1, 9, 55, 0)).getTime());
        Lesson lesson3 = new Lesson( "teacher3", "science",
                (new GregorianCalendar(2019, 11, 3, 0, 0, 0)).getTime(),
                (new GregorianCalendar(2019, 11, 3, 24, 0, 0)).getTime());
        //Test cardview
        // TODO connect to the database
        Request request1 = new Request("impedovo", "decarolis", 1);
        Request request2 = new Request("teacher2", "teacher3", 2);
        Request request3 = new Request("impedovo", "decarolis", 3);

        //insertRequest(request1,request2, request3);
        //insertLesson(lesson, lesson2, lesson3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exist
        db.execSQL(LessonContract.DELETE_TABLE);
        db.execSQL(RequestContract.DELETE_TABLE);
        db.execSQL(UserContract.DELETE_TABLE);
        db.execSQL(SubjectContract.DELETE_TABLE);
        db.execSQL(UnavailabilityContract.DELETE_TABLE);
        // Create tables again
        onCreate(db);
    }

    // TODO make argument single
    public void insertRequest(Request... requests){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        for (Request request : requests){
            cValues.put(RequestContract.KEY_FROM_TEACHER, request.fromTeacher);
            cValues.put(RequestContract.KEY_TO_TEACHER, request.toTeacher);
            cValues.put(RequestContract.KEY_LESSON, request.lesson);

            db.insert(RequestContract.TABLE_NAME, null, cValues);
        }

        db.close();
    }

    public ArrayList<Request> getAllRequests(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Request> requests = new ArrayList<>();
        Cursor cursor = db.rawQuery(RequestContract.GET_ALL_REQUESTS, null);
        String query = "SELECT * FROM " + RequestContract.TABLE_NAME
                + " INNER JOIN " + LessonContract.TABLE_NAME
                + " ON " + RequestContract.KEY_LESSON + " = "
                + " = " + LessonContract.KEY_ID;
        while(cursor.moveToNext()){
            Request request = new Request(
                    cursor.getInt(cursor.getColumnIndex(RequestContract.KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(RequestContract.KEY_FROM_TEACHER)),
                    cursor.getString(cursor.getColumnIndex(RequestContract.KEY_TO_TEACHER)),
                    cursor.getInt(cursor.getColumnIndex(RequestContract.KEY_LESSON)));
            requests.add(request);
        }

        cursor.close();

        return requests;
    }

    public void deleteRequest(Request request){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(RequestContract.TABLE_NAME, RequestContract.KEY_ID+" = ?",new String[]{String.valueOf(request.id)});
        db.close();
    }

    public void insertLesson(Lesson... lessons){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ITALY);
        for (Lesson lesson : lessons){
            cValues.put(LessonContract.KEY_TEACHER, lesson.teacher);
            cValues.put(LessonContract.KEY_SUBJECT, lesson.subject);
            cValues.put(LessonContract.KEY_FROM_TIME, dateFormat.format(lesson.fromTime));
            cValues.put(LessonContract.KEY_TO_TIME, dateFormat.format(lesson.toTime));

            db.insert(LessonContract.TABLE_NAME, null, cValues);
        }

        db.close();
    }

    public ArrayList<Lesson> getAllLessonFor(Date day) {
        SQLiteDatabase db = this.getReadableDatabase();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        ArrayList<Lesson> lessons = new ArrayList<>();

        // SELECT * FROM Lesson WHERE from_time = 'date%'
        String rawQuery = "SELECT * FROM " + LessonContract.TABLE_NAME
                + " WHERE " + LessonContract.KEY_FROM_TIME
                + " LIKE '"+ dateFormat.format(day.getTime()) + "%'";
        Cursor cursor = db.rawQuery(rawQuery, null);

        // Changed the date format for saving properly the date
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        while(cursor.moveToNext()){
            Lesson lesson = null;
            try {
                lesson = new Lesson(
                        cursor.getInt(cursor.getColumnIndex(LessonContract.KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(LessonContract.KEY_TEACHER)),
                        cursor.getString(cursor.getColumnIndex(LessonContract.KEY_SUBJECT)),
                        dateFormat.parse(cursor.getString(cursor.getColumnIndex(LessonContract.KEY_FROM_TIME))),
                        dateFormat.parse(cursor.getString(cursor.getColumnIndex(LessonContract.KEY_TO_TIME))));
            } catch (ParseException e) {
                // If there is a parse error skip the lesson
                e.printStackTrace();
                continue;
            }
            lessons.add(lesson);
        }
        cursor.close();

        return lessons;
    }


    public Lesson getLesson( int id){
        SQLiteDatabase db = this.getReadableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());

        // SELECT * FROM Lesson WHERE from_time = 'date%'
        String rawQuery = "SELECT * FROM " + LessonContract.TABLE_NAME
                + " WHERE " + LessonContract.KEY_ID + " = " + id;
        Cursor cursor = db.rawQuery(rawQuery, null);
        cursor.moveToFirst();

        Lesson lesson = null;
        try {
            lesson = new Lesson(
                    cursor.getInt(cursor.getColumnIndex(LessonContract.KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(LessonContract.KEY_TEACHER)),
                    cursor.getString(cursor.getColumnIndex(LessonContract.KEY_SUBJECT)),
                    dateFormat.parse(cursor.getString(cursor.getColumnIndex(LessonContract.KEY_FROM_TIME))),
                    dateFormat.parse(cursor.getString(cursor.getColumnIndex(LessonContract.KEY_TO_TIME))));
        } catch (ParseException e) {
            // If there is a parse error skip the lesson
            e.printStackTrace();
        }

        cursor.close();
        return lesson;
    }

    public Result<User> login(String username, String password) {

        // TODO: handle loggedInUser authentication
        switch(username){
            case "scheduler":
                User scheduler =
                        new User(
                                "Scheduler",
                                User.Type.SCHEDULER);
                return new Result.Success<>(scheduler);
            case "impedovo":
                User impedovo =
                        new User(
                                "Donato Impedovo",
                                User.Type.TEACHER);
                return new Result.Success<>(impedovo);
            case "roselli":
                User roselli =
                        new User(
                                "Teresa Roselli",
                                User.Type.TEACHER);
                return new Result.Success<>(roselli);
        }

        return new Result.Error(new Exception("Login Failed"));
    }


    public void logout() {
        // TODO: revoke authentication
    }

}
