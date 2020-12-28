package com.example.languidedemo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if(instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        if(db == null) this.db = openHelper.getWritableDatabase();
    }

    public void close() {
        if(db != null) this.db.close();
    }

    public boolean insert(String userName, String email, String password) {
        ContentValues values = new ContentValues();
        values.put("username", userName);
        values.put("email", email);
        values.put("password", password);
        return this.db.insert("users", null, values) != -1;
    }

    public boolean findUser(String email, String password) {
        c = db.rawQuery("select password from users where email = ?", new String[]{email});
        if(c.getCount() <= 0) {
            return false;
        } else {
            c.moveToFirst();
            return c.getString(0).equals(password);
        }
    }
}
