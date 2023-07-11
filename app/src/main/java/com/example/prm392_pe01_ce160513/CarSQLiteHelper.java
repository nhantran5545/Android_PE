package com.example.prm392_pe01_ce160513;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CarSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CarSQLite.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "Cars";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_PRICE = "price";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_MODEL + " TEXT NOT NULL, " +
            COLUMN_PRICE + " INTEGER NOT NULL)";

    public CarSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

