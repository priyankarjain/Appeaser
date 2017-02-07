package com.example.priya.servicetutorial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priya on 1/24/2017.
 */

public class ChatDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbhelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,MySQLiteHelper.COLUMN_SOURCE,
            MySQLiteHelper.COLUMN_TYPE,MySQLiteHelper.COLUMN_MESSAGE};

    public ChatDataSource(Context context){
        dbhelper = new MySQLiteHelper(context);
    }
    public void open() throws SQLException{
        database = dbhelper.getWritableDatabase();
    }

    public void close(){
        dbhelper.close();
    }

    public ChatModel createChat(String source,String type,String message){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_SOURCE,source);
        values.put(MySQLiteHelper.COLUMN_TYPE,type);
        values.put(MySQLiteHelper.COLUMN_MESSAGE,message);
        long insertId = database.insert(MySQLiteHelper.TABLE_CHATS,null,values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CHATS,allColumns,
                MySQLiteHelper.COLUMN_ID + "=" +insertId,null,null,null,null);
        cursor.moveToFirst();
        ChatModel chat = cursorToChat(cursor);
        cursor.close();
        return chat;
    }

    private ChatModel cursorToChat(Cursor cursor) {
        ChatModel cm = new ChatModel();
        cm.setId(cursor.getLong(0));
        cm.setSource(cursor.getString(1));
        cm.setType(cursor.getString(2));
        cm.setMessage(cursor.getString(3));

        return cm;
    }

    public void deleteChat(ChatModel chat){
        long id = chat.getId();
        database.delete(MySQLiteHelper.TABLE_CHATS,MySQLiteHelper.COLUMN_ID + "=" +id,null);
    }

    public List<ChatModel> getAllChats(String source){
        List<ChatModel> chats = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CHATS,allColumns,
                MySQLiteHelper.COLUMN_SOURCE + "=\""+source+"\"",null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            ChatModel cm = cursorToChat(cursor);
            chats.add(cm);
            cursor.moveToNext();
        }
        cursor.close();
        return chats;
    }
}
