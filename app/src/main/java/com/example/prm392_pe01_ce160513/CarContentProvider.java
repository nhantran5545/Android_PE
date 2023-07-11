package com.example.prm392_pe01_ce160513;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CarContentProvider extends ContentProvider {


    private static final String AUTHORITY = "com.example.carcontentprovider";
    private static final String TABLE_NAME = "Cars";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    private static final int CARS = 1;
    private static final int CAR_ID = 2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, CARS);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", CAR_ID);
    }

    private CarSQLiteHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new CarSQLiteHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;

        switch (uriMatcher.match(uri)) {
            case CARS:
                cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CAR_ID:
                selection = "id = " + uri.getLastPathSegment();
                cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case CARS:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE_NAME;
            case CAR_ID:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = db.insert(TABLE_NAME, null, values);

        if (rowId > 0) {
            Uri insertUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(insertUri, null);
            return insertUri;
        }

        throw new SQLiteException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;

        switch (uriMatcher.match(uri)) {
            case CARS:
                count = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case CAR_ID:
                selection = "id = " + uri.getLastPathSegment();
                count = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;

        switch (uriMatcher.match(uri)) {
            case CARS:
                count = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            case CAR_ID:
                selection = "id = " + uri.getLastPathSegment();
                count = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }
}

