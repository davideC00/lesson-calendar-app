package it.uniba.di.sms.orariolezioni.data;

import android.provider.BaseColumns;

class DbContract {
    static final int DB_VERSION = 2;
    static final String DB_NAME = "orarioLezionidb";

    private DbContract(){}

    static class Lesson implements BaseColumns{
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

    static class Request implements BaseColumns{
        static final String TABLE_NAME = "Request";
        static final String KEY_ID = "id";
        static final String KEY_FROM_TEACHER = "from_teacher";
        static final String KEY_TO_TEACHER = "to_teacher";
        static final String KEY_FROM_TIME = "from_time";
        static final String KEY_TO_TIME = "to_time";

        static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME+ "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_FROM_TEACHER + " VARCHAR(40),"
            + KEY_TO_TEACHER + " VARCHAR(40),"
            + KEY_FROM_TIME + " DATETIME,"
            + KEY_TO_TIME + " DATETIME " + ")";
        static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        static final String GET_ALL_REQUESTS = "SELECT * FROM " + TABLE_NAME;
    }
}
