package ru.eugene.exam1.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by eugene on 1/21/15.
 */
public class ToDoProvider extends ContentProvider {
    private ToDoHelper dbHelper;

    private static final String AUTHORITY = "ru.eugene.exam1.db";
    private static final String PATH_TODO = "to_do";
    private static final String PATH_LABEL = "label";

    public static final Uri CONTENT_URI_TODO = Uri.parse("content://" + AUTHORITY
            + "/" + PATH_TODO);

    public static final Uri CONTENT_URI_LABEL = Uri.parse("content://" + AUTHORITY
            + "/" + PATH_LABEL);

    private static final int TODO = 10;
    private static final int LABEL = 30;
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, PATH_TODO, TODO);
        sURIMatcher.addURI(AUTHORITY, PATH_LABEL, LABEL);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new ToDoHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int match = sURIMatcher.match(uri);
        Cursor result = db.query(getTableName(uri), projection,
                selection, selectionArgs, null, null, sortOrder);

        result.setNotificationUri(getContext().getContentResolver(), uri);
        return result;
    }

    private String getTableName(Uri uri) {
        int match = sURIMatcher.match(uri);
        switch (match) {
            case TODO:
                return ToDoSource.TABLE_NAME;
            case LABEL:
                return LabelSource.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long id = db.insert(getTableName(uri), null, values);
        if (id > 0) {
            uri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int id = db.delete(getTableName(uri), selection, selectionArgs);
        if (id > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return id;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int id = db.update(getTableName(uri), values, selection, selectionArgs);
        if (id > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return id;
    }
}
