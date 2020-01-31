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
import it.uniba.di.sms.orariolezioni.data.model.Subject;
import it.uniba.di.sms.orariolezioni.data.model.Unavailability;
import it.uniba.di.sms.orariolezioni.data.model.User;

import static it.uniba.di.sms.orariolezioni.data.DbContract.DB_NAME;
import static it.uniba.di.sms.orariolezioni.data.DbContract.DB_VERSION;
import static it.uniba.di.sms.orariolezioni.data.DbContract.LessonContract;
import static it.uniba.di.sms.orariolezioni.data.DbContract.RequestContract;
import static it.uniba.di.sms.orariolezioni.data.DbContract.SubjectContract;
import static it.uniba.di.sms.orariolezioni.data.DbContract.UnavailabilityContract;
import static it.uniba.di.sms.orariolezioni.data.DbContract.UserContract;


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

        populateDatabase(db);
    }


    public void populateDatabase(SQLiteDatabase db) {

        /* Un comment if you want add lessons too
        Lesson lesson = new Lesson("teacher1", "math",
                (new GregorianCalendar(2019, 11, 2, 15, 30, 0)).getTime(),
                (new GregorianCalendar(2019, 11, 2, 17, 48, 0)).getTime());
        Lesson lesson2 = new Lesson( "teacher2", "science",
                (new GregorianCalendar(2019, 11, 1, 8, 30, 0)).getTime(),
                (new GregorianCalendar(2019, 11, 1, 9, 55, 0)).getTime());
        Lesson lesson3 = new Lesson( "teacher3", "science",
                (new GregorianCalendar(2019, 11, 3, 0, 0, 0)).getTime(),
                (new GregorianCalendar(2019, 11, 3, 24, 0, 0)).getTime());

         */

        User user0 = new User("scheduler", "scheduler");
        User user1 = new User("impedovo", "teacher");
        User user2 = new User("teacher", "teacher");
        User user3 = new User("decarolis", "teacher");
        ArrayList<User> users = new ArrayList<>();
        users.add(user0);
        users.add(user1);
        users.add(user2);
        users.add(user3);

        Subject sub1 = new Subject("science", user2.username);
        Subject sub2 = new Subject("math", user1.username);
        Subject sub3 = new Subject("informatics", user2.username);
        Subject sub4 = new Subject("piu", user3.username);
        ArrayList<Subject> subjects = new ArrayList<>();
        subjects.add(sub1);
        subjects.add(sub2);
        subjects.add(sub3);
        subjects.add(sub4);

        ContentValues cValues = new ContentValues();

        // Insert Subjects
        for(Subject subject : subjects){
            cValues.clear();
            cValues.put(SubjectContract.KEY_NAME, subject.name);
            cValues.put(SubjectContract.KEY_TEACHER, subject.teacher);
            db.insert(SubjectContract.TABLE_NAME, null, cValues);
        }

        // Insert Users
        for (User user : users){
            cValues.clear();
            cValues.put(UserContract.KEY_USERNAME, user.username);
            cValues.put(UserContract.KEY_PASS, "pass");
            cValues.put(UserContract.KEY_TYPE, user.type);

            db.insert(UserContract.TABLE_NAME, null, cValues);
        }
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


    private void insertSubject(Subject... subjects) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        for (Subject subject: subjects){
            cValues.put(SubjectContract.KEY_NAME, subject.name);
            cValues.put(SubjectContract.KEY_TEACHER, subject.teacher);

            db.insert(SubjectContract.TABLE_NAME, null, cValues);
        }
        db.close();
    }

    private void insertUser(User... users) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        for (User user : users){
            cValues.put(UserContract.KEY_USERNAME, user.username);
            cValues.put(UserContract.KEY_PASS, "pass");
            cValues.put(UserContract.KEY_TYPE, user.type);

            db.insert(UserContract.TABLE_NAME, null, cValues);
        }
        db.close();
    }

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

    public ArrayList<String> getTeacherSubjects(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> subjects = new ArrayList<>();

        String query = "SELECT " + SubjectContract.KEY_NAME + " FROM " + SubjectContract.TABLE_NAME
                + " WHERE " + SubjectContract.KEY_TEACHER + " = '" + username + "'";

        Cursor cursor = db.rawQuery(query, null);
        while(cursor.moveToNext()){
            subjects.add(cursor.getString(cursor.getColumnIndex(SubjectContract.KEY_NAME)));
        }

        cursor.close();

        return  subjects;
    }

    public ArrayList<Request> getAllRequests(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Request> requests = new ArrayList<>();
        Cursor cursor = db.rawQuery(RequestContract.GET_ALL_REQUESTS, null);
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

    public ArrayList<Request> getRequestsOf(String teacher) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Request> requests = new ArrayList<>();

        String rawQuery = "SELECT * FROM " + RequestContract.TABLE_NAME
                + " WHERE " + RequestContract.KEY_TO_TEACHER + " = '" + teacher + "'";
        Cursor cursor = db.rawQuery(rawQuery, null);

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

    public void acceptRequest(Request request){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(LessonContract.KEY_TEACHER, request.toTeacher);
        // cv.put(LessonContract.KEY_SUBJECT, request.subject); Currently commented because it is not supported
        db.update(LessonContract.TABLE_NAME, cv, LessonContract.KEY_ID+" = ?", new String[]{String.valueOf(request.lesson)});
        deleteRequest(request);
        db.close();
    }

    public void deleteRequest(Request request){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(RequestContract.TABLE_NAME, RequestContract.KEY_ID+" = ?",new String[]{String.valueOf(request.id)});
        db.close();
    }

    public void deleteLesson(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LessonContract.TABLE_NAME, LessonContract.KEY_ID+" = ?",new String[]{String.valueOf(id)});
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

    public void insertUnavailability(Unavailability... unavailability){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ITALY);
        for (Unavailability u : unavailability){
            cValues.put(UnavailabilityContract.KEY_TEACHER, u.teacher);
            cValues.put(UnavailabilityContract.KEY_FROM_TIME, dateFormat.format(u.fromTime));
            cValues.put(UnavailabilityContract.KEY_TO_TIME, dateFormat.format(u.toTime));

            db.insert(UnavailabilityContract.TABLE_NAME, null, cValues);
        }

        db.close();
    }

    public void deleteUnavailability(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UnavailabilityContract.TABLE_NAME, UnavailabilityContract.KEY_ID+" = ?",new String[]{String.valueOf(id)});
        db.close();
    }

    public ArrayList<Unavailability> getAllUnavailabilityFor(Date day) {
        SQLiteDatabase db = this.getReadableDatabase();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

        // SELECT * FROM Unavailability WHERE from_time = 'date%'
        String rawQuery = "SELECT * FROM " + UnavailabilityContract.TABLE_NAME
                + " WHERE " + UnavailabilityContract.KEY_FROM_TIME
                + " LIKE '"+ dateFormat.format(day.getTime()) + "%'";
        Cursor cursor = db.rawQuery(rawQuery, null);

        return extractUnavailabilityFrom(cursor);
    }

    public ArrayList<Unavailability> getAllUnavailabilityFor(Date day, String teacher){
        SQLiteDatabase db = this.getReadableDatabase();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

        // SELECT * FROM Unavailability WHERE from_time = 'date%'
        String rawQuery = "SELECT * FROM " + UnavailabilityContract.TABLE_NAME
                + " WHERE " + UnavailabilityContract.KEY_FROM_TIME
                + " LIKE '"+ dateFormat.format(day.getTime()) + "%'"
                + " AND " + UnavailabilityContract.KEY_TEACHER + " = '"  + teacher + "'";
        Cursor cursor = db.rawQuery(rawQuery, null);

        return extractUnavailabilityFrom(cursor);
    }

    private ArrayList<Unavailability> extractUnavailabilityFrom(Cursor cursor){

        ArrayList<Unavailability> unavailability = new ArrayList<>();

        // Changed the date format for saving properly the date
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        while(cursor.moveToNext()){
            Unavailability u = null;
            try {
                u = new Unavailability(
                        cursor.getInt(cursor.getColumnIndex(UnavailabilityContract.KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(UnavailabilityContract.KEY_TEACHER)),
                        dateFormat.parse(cursor.getString(cursor.getColumnIndex(UnavailabilityContract.KEY_FROM_TIME))),
                        dateFormat.parse(cursor.getString(cursor.getColumnIndex(UnavailabilityContract.KEY_TO_TIME))));
            } catch (ParseException e) {
                // If there is a parse error skip the unavailability
                e.printStackTrace();
                continue;
            }
            unavailability.add(u);
        }
        cursor.close();

        return unavailability;
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

        switch(username){
            case "scheduler":
                User scheduler =
                        new User(
                                "scheduler",
                                "scheduler");
                return new Result.Success<>(scheduler);
            case "impedovo":
                User impedovo =
                        new User(
                                "impedovo",
                                "teacher");
                return new Result.Success<>(impedovo);
            case "decarolis":
                User decarolis =
                        new User(
                                "decarolis",
                                "teacher");
                return new Result.Success<>(decarolis);
        }

        return new Result.Error(new Exception("Login Failed"));
    }


    public ArrayList<String> getTeachers() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> teachers = new ArrayList<>();
        String query = "SELECT * FROM " + UserContract.TABLE_NAME
                +   " WHERE " + UserContract.KEY_TYPE + " = 'teacher'";
        Cursor cursor = db.rawQuery(query, null);
        while(cursor.moveToNext()){
            User teacher = new User(
                    cursor.getString(cursor.getColumnIndex(UserContract.KEY_USERNAME)),
                    cursor.getString(cursor.getColumnIndex(UserContract.KEY_TYPE)));
            teachers.add(teacher.username);
        }

        cursor.close();

        return teachers;
    }

    public String getTeacherForLesson(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String teacher = null;

        String query = "SELECT " + LessonContract.KEY_TEACHER + " FROM " + LessonContract.TABLE_NAME
                + " WHERE " + LessonContract.KEY_ID + " = '" + id + "'";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            teacher = cursor.getString(cursor.getColumnIndex(LessonContract.KEY_TEACHER));
        }

        cursor.close();

        return  teacher;
    }


}
