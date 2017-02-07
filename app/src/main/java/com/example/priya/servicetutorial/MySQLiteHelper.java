package com.example.priya.servicetutorial;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by priya on 1/24/2017.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_CHATS = "chats";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SOURCE = "source";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_MESSAGE = "message";

    private static final String DB_NAME = "chats.db";
    public static final int DB_VERSION = 1;

    public static final String DATABASE_CREATE = "create table "+TABLE_CHATS+"( "+COLUMN_ID
            +" integer primary key autoincrement, "+
            COLUMN_SOURCE + " text not null, "+
            COLUMN_TYPE + " text not null, "+
            COLUMN_MESSAGE + " text not null);";

    public MySQLiteHelper(Context context) {
        super(context, DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CHATS);
        onCreate(db);
    }
}
