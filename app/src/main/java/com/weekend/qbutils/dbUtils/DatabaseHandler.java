package com.weekend.qbutils.dbUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static SQLiteDatabase writableDb;
    // All Static variables
    // Database Version
    protected Object lock = new Object();

    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static String DATABASE_NAME = "WeekendCustomer_chat_DB";
    public static DatabaseHandler mInstance;

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHandler(context);
            mInstance.getWritableDatabase();
        }
        return mInstance;
    }

    @Override
    public void close() {
        super.close();
        mInstance = null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /* create contact table */
//        db.execSQL(TableChatUser.CREATE_TABLE);
        db.execSQL(TableChatList.CREATE_TABLE);
        db.execSQL(TableMessages.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /* update table query */
        try {
            if (oldVersion != DATABASE_VERSION) {
//                db.execSQL("DROP TABLE IF EXISTS " + TableChatUser.TABLENAME);
                db.execSQL("DROP TABLE IF EXISTS " + TableChatList.TABLENAME);
                db.execSQL("DROP TABLE IF EXISTS " + TableMessages.TABLENAME);
                onCreate(db);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public SQLiteDatabase getWritableDatabase() {
        if (writableDb == null || !writableDb.isOpen()) {
            writableDb = super.getWritableDatabase();
        }
        return writableDb;
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        if (writableDb == null || !writableDb.isOpen()) {
            writableDb = super.getReadableDatabase();
        }
        return writableDb;
    }

    public synchronized int clearTable(String tableName) {
        //TableChatUser.URI, null, where, args, null
        return getWritableDatabase().delete(tableName, null, null);
    }

    public synchronized void clearDB() {
//        getWritableDatabase().execSQL("delete from " + TableChatUser.TABLENAME);
        getWritableDatabase().execSQL("delete from " + TableChatList.TABLENAME);
        getWritableDatabase().execSQL("delete from " + TableMessages.TABLENAME);
    }

    public synchronized void clearSingleTable(String tableName) {
        getWritableDatabase().execSQL("DELETE FROM " + tableName + ";");
    }

    public long insertData(String tableName, ContentValues values) {
        return getWritableDatabase().insert(tableName, null, values);
    }

    public int updateData(String tableName, ContentValues values, String where, String[] args) {
        return getWritableDatabase().update(tableName, values, where, args);
    }

    public int deleteData(String tableName, String where, String[] args) {
        return getWritableDatabase().delete(tableName, where, args);
    }

    public Cursor selectData(String tableName) {
        //TableChatUser.URI, null, where, args, null
        return getWritableDatabase().query(tableName, null, null, null, null, null, null);
    }

    public Cursor selectData(String tableName, String where, String[] args) {
        //TableChatUser.URI, null, where, args, null
        return getWritableDatabase().query(tableName, null, where, args, null, null, null);
    }

    public Cursor selectData(String selectQuery, boolean rowQuery) {
        //TableChatUser.URI, null, where, args, null
        return getWritableDatabase().rawQuery(selectQuery, null);
    }
}
