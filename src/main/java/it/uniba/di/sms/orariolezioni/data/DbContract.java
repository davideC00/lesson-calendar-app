package it.uniba.di.sms.orariolezioni.data;

import android.provider.BaseColumns;

class DbContract {
    static final int DB_VERSION = 3;
    // TODO change orarioLezioniDb to ScheduleForLesson
    static final String DB_NAME = "orarioLezionidb";

    private DbContract(){}

    static class UserContract implements  BaseColumns{
        static final String TABLE_NAME = "User";
        static final String KEY_USERNAME = "username";
        static final String KEY_PASS = "password";
        static final String KEY_TYPE = "type";

        static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_USERNAME + " VARCHAR(40) PRIMARY KEY,"
                + KEY_PASS + " VARCHAR(40),"
                + KEY_TYPE + " VARCHAR(40))";
        static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    static class SubjectContract implements BaseColumns{
        static final String TABLE_NAME = "Subject";
        static final String KEY_NAME = "name";
        static final String KEY_TEACHER = "teacher";

        static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_NAME + " VARCHAR(40),"
                + KEY_TEACHER + " VARCHAR(40),"
                + "PRIMARY KEY(" + KEY_NAME  + "," + KEY_TEACHER + "))";
        static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    static class UnavailabilityContract implements  BaseColumns{
        static final String TABLE_NAME = "Unavailability";
        static final String KEY_ID = "id";
        static final String KEY_TEACHER = "teacher";
        static final String KEY_FROM_TIME = "from_time";
        static final String KEY_TO_TIME = "to_time";

        static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TEACHER + " VARCHAR(40),"
                + KEY_FROM_TIME + " DATETIME,"
                + KEY_TO_TIME + " DATETIME " + ")";
        static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    static class LessonContract implements BaseColumns{
        static final String TABLE_NAME = "Lesson";
        static final String KEY_ID = "id";
        static final String KEY_TEACHER = "teacher";
        static final String KEY_SUBJECT = "subject";
        static final String KEY_FROM_TIME = "from_time";
        static final String KEY_TO_TIME = "to_time";

        static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TEACHER + " VARCHAR(40),"
            + KEY_SUBJECT + " VARCHAR(40),"
            + KEY_FROM_TIME + " DATETIME,"
            + KEY_TO_TIME + " DATETIME " + ")";
        static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    static class RequestContract implements BaseColumns{
        static final String TABLE_NAME = "Request";
        static final String KEY_ID = "id";
        static final String KEY_FROM_TEACHER = "from_teacher";
        static final String KEY_TO_TEACHER = "to_teacher";
        static final String KEY_LESSON = "lesson";

        static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME+ "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_FROM_TEACHER + " VARCHAR(40),"
            + KEY_TO_TEACHER + " VARCHAR(40),"
            + KEY_LESSON + " INTEGER )";
        static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        static final String GET_ALL_REQUESTS = "SELECT * FROM " + TABLE_NAME;
    }
}
