package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

interface DBContract {
    static final String TABLE_NAME="GAME";
    static final String COL_ID="ID";
    static final String COL_TITLE="GTITLE";
    static final String COL_SUB_TITLE="WTITLE";
    static final String COL_STAMP="STAMP";
    static final String COL_SITE="SITE";
    static final String COL_S_WALKTHROUGH="SWT";
    static final String COL_DAY="DAY";
    static final String COL_SPECIAL="SPECIAL";
    static final String COL_BOOKMARK="BOOKMARK";

    static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + "(" +
            COL_ID + " INTEGER NOT NULL PRIMARY KEY, " +
            COL_TITLE + " TEXT NOT NULL, " +
            COL_SUB_TITLE + " TEXT NOT NULL, " +
            COL_STAMP + " TEXT, " +
            COL_SITE + " TEXT, " +
            COL_S_WALKTHROUGH + " TEXT NOT NULL, " +
            COL_DAY + " TEXT, " +
            COL_SPECIAL + " TEXT," +
            COL_BOOKMARK + " INTEGER DEFAULT 0 NOT NULL)";
    static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    static final String SQL_LOAD = "SELECT * FROM " + TABLE_NAME;
    static final String SQL_SELECT = "SELECT * FROM "  + TABLE_NAME + " WHERE " + COL_DAY + "=?";
    static final String SQL_SELECT2 = "SELECT * FROM "  + TABLE_NAME + " WHERE " + COL_SPECIAL + " LIKE ?";
    static final String SQL_SELECT3 = "SELECT * FROM "  + TABLE_NAME + " WHERE " + COL_BOOKMARK + "=?";
    static final String SQL_SELECT4 = "SELECT " +COL_BOOKMARK+ " FROM "  + TABLE_NAME + " WHERE " + COL_ID + "=?";
    static final String SQL_SELECT5 = "SELECT * FROM "  + TABLE_NAME + " WHERE " + COL_TITLE + " LIKE ?";
}

class DBHelper extends SQLiteOpenHelper {
    static final String DB_FILE = "game.db";
    static final int DB_VERSION = 1;

    DBHelper(Context context) {
        super(context, DB_FILE, null, DB_VERSION); // 세번째 인자 : cursor factory
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBContract.SQL_DROP_TABLE);
        onCreate(db);
    }
}