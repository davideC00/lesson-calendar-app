package it.uniba.di.sms.orariolezioni.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import it.uniba.di.sms.orariolezioni.data.model.Request;

public class DbHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "orarioLezionidb";
    private static final String TABLE_REQUEST = "Request";
    private static final String KEY_ID = "id";
    private static final String KEY_FROM_TEACHER = "from_teacher";
    private static final String KEY_TO_TEACHER = "to_teacher";
    private static final String KEY_FROM_TIME = "from_time";
    private static final String KEY_TO_TIME = "to_time";

    public DbHandler(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_REQUEST + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_FROM_TEACHER + " VARCHAR(40),"
                + KEY_TO_TEACHER + " VARCHAR(40),"
                + KEY_FROM_TIME + " DATETIME,"
                + KEY_TO_TIME + " DATETIME " + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUEST);
        // Create tables again
        onCreate(db);
    }

    // TODO make argument single
    public void insertRequest(Request... requests){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        for (Request request : requests){
            cValues.put(KEY_FROM_TEACHER, request.fromTeacher);
            cValues.put(KEY_TO_TEACHER, request.toTeacher);
            cValues.put(KEY_FROM_TIME, request.fromTime);
            cValues.put(KEY_TO_TIME, request.toTime);

            db.insert(TABLE_REQUEST, null, cValues);
        }

        db.close();
    }

    public ArrayList<Request> getAllRequests(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Request> requests = new ArrayList<>();
        String rawQuery = "SELECT * FROM " + TABLE_REQUEST;
        Cursor cursor = db.rawQuery(rawQuery, null);
        while(cursor.moveToNext()){
            Request request = new Request(
                    cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_FROM_TEACHER)),
                    cursor.getString(cursor.getColumnIndex(KEY_TO_TEACHER)),
                    cursor.getString(cursor.getColumnIndex(KEY_FROM_TIME)),
                    cursor.getString(cursor.getColumnIndex(KEY_TO_TIME))
            );
            requests.add(request);
        }

        return requests;
    }

    public void deleteRequest(Request request){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REQUEST, KEY_ID+" = ?",new String[]{String.valueOf(request.id)});
        db.close();
    }
}
