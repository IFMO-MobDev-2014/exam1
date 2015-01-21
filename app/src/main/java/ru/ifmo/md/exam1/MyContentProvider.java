package ru.ifmo.md.exam1;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {
    private static String AUTHORITY = "ru.ifmo.md.exam1.MyContentProvider";
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + DataBaseHelper.TABLE_NAME);
    private DataBaseHelper dataBaseHelper;

    @Override
    public boolean onCreate() {
        dataBaseHelper = new DataBaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(uri.getLastPathSegment(), new String[]{"_id", "date", "name", "description", "category"}, null, null, null, null, sortOrder);
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        String name = uri.getLastPathSegment();
        long id = sqLiteDatabase.insert(name, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse("content://" + AUTHORITY + "/" + name + "/" + Long.toString(id));
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        sqLiteDatabase.delete(uri.getLastPathSegment(), "_id=" + (Long.parseLong(selection)), null);
        getContext().getContentResolver().notifyChange(uri, null);
        return 1;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        sqLiteDatabase.update(uri.getLastPathSegment(), values, "_id=" + values.getAsLong("_id"), selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return 1;
    }

}