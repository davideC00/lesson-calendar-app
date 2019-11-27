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
import java.util.Locale;

import it.uniba.di.sms.orariolezioni.data.model.Lesson;
import it.uniba.di.sms.orariolezioni.data.model.Request;

import static it.uniba.di.sms.orariolezioni.data.DbContract.*;


public class DbHandler extends SQLiteOpenHelper {



    public DbHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.Lesson.CREATE_TABLE);
        db.execSQL(DbContract.Request.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exist
        db.execSQL(DbContract.Lesson.DELETE_TABLE);
        db.execSQL(DbContract.Request.DELETE_TABLE);
        // Create tables again
        onCreate(db);
    }

    // TODO make argument single
    public void insertRequest(Request... requests){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        for (Request request : requests){
            cValues.put(DbContract.Request.KEY_FROM_TEACHER, request.fromTeacher);
            cValues.put(DbContract.Request.KEY_TO_TEACHER, request.toTeacher);
            cValues.put(DbContract.Request.KEY_FROM_TIME, request.fromTime);
            cValues.put(DbContract.Request.KEY_TO_TIME, request.toTime);

            db.insert(DbContract.Request.TABLE_NAME, null, cValues);
        }

        db.close();
    }

    public ArrayList<Request> getAllRequests(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Request> requests = new ArrayList<>();
        Cursor cursor = db.rawQuery(DbContract.Request.GET_ALL_REQUESTS, null);
        while(cursor.moveToNext()){
            Request request = new Request(
                    cursor.getInt(cursor.getColumnIndex(DbContract.Request.KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(DbContract.Request.KEY_FROM_TEACHER)),
                    cursor.getString(cursor.getColumnIndex(DbContract.Request.KEY_TO_TEACHER)),
                    cursor.getString(cursor.getColumnIndex(DbContract.Request.KEY_FROM_TIME)),
                    cursor.getString(cursor.getColumnIndex(DbContract.Request.KEY_TO_TIME))
            );
            requests.add(request);
        }

        cursor.close();

        return requests;
    }

    public void deleteRequest(Request request){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DbContract.Request.TABLE_NAME, DbContract.Request.KEY_ID+" = ?",new String[]{String.valueOf(request.id)});
        db.close();
    }

    public void insertLesson(Lesson... lessons){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Lesson");
        ContentValues cValues = new ContentValues();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ITALY);
        for (Lesson lesson : lessons){
            cValues.put(DbContract.Lesson.KEY_TEACHER, lesson.teacher);
            cValues.put(DbContract.Lesson.KEY_SUBJECT, lesson.subject);
            cValues.put(DbContract.Lesson.KEY_FROM_TIME, dateFormat.format(lesson.fromTime));
            cValues.put(DbContract.Lesson.KEY_TO_TIME, dateFormat.format(lesson.toTime));

            db.insert(DbContract.Lesson.TABLE_NAME, null, cValues);
        }

        db.close();
    }

    public ArrayList<Lesson> getAllLessonFor(Date day) {
        SQLiteDatabase db = this.getReadableDatabase();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.ITALY);
        ArrayList<Lesson> lessons = new ArrayList<>();

        // SELECT * FROM Lesson WHERE from_time = 'date%'
        String rawQuery = "SELECT * FROM " + DbContract.Lesson.TABLE_NAME
                + " WHERE " + DbContract.Lesson.KEY_FROM_TIME
                + " LIKE '"+ dateFormat.format(day.getTime()) + "%'";
        Cursor cursor = db.rawQuery(rawQuery, null);

        // Changed the date format for saving properly the date
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ITALY);
        while(cursor.moveToNext()){
            Lesson lesson = null;
            try {
                lesson = new Lesson(
                        cursor.getInt(cursor.getColumnIndex(DbContract.Request.KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(DbContract.Lesson.KEY_TEACHER)),
                        cursor.getString(cursor.getColumnIndex(DbContract.Lesson.KEY_SUBJECT)),
                        dateFormat.parse(cursor.getString(cursor.getColumnIndex(DbContract.Lesson.KEY_FROM_TIME))),
                        dateFormat.parse(cursor.getString(cursor.getColumnIndex(DbContract.Lesson.KEY_TO_TIME)))
                );
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
}
