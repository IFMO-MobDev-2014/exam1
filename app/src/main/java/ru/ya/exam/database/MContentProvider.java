package ru.ya.exam.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class MContentProvider extends ContentProvider {
    MSQLiteHelper msqLiteHelper;
    public static final String AUTHORITY = "ru.ya.exam.database.MContentProvider";

    public static final String PATH_ITEM = "P_ITEM";
    public static final String PATH_LABEL = "P_LABEL";
    public static final String PATH_ALL_LABEL = "P_ALL_LABEL";



    public static final Uri ITEM_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH_ITEM);
    public static final Uri LABEL_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH_LABEL);
    public static final Uri ALL_LABEL_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH_ALL_LABEL);

    private static final UriMatcher uriMatcher = new UriMatcher(0);

    static {
       uriMatcher.addURI(AUTHORITY, PATH_ITEM, 1);
       uriMatcher.addURI(AUTHORITY, PATH_LABEL, 2);
       uriMatcher.addURI(AUTHORITY, PATH_ALL_LABEL, 3);
    }

    @Override
    public boolean onCreate() {
        msqLiteHelper = new MSQLiteHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        if (uriMatcher.match(uri) == 1)
            cursor = msqLiteHelper.getReadableDatabase().query(MSQLiteHelper.ITEM_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        else if (uriMatcher.match(uri) == 2)
            cursor = msqLiteHelper.getReadableDatabase().query(MSQLiteHelper.LABEL_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        else
            cursor = msqLiteHelper.getReadableDatabase().query(MSQLiteHelper.ALL_LABEL_TABLE, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        throw new Error();
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Long id;
        if (uriMatcher.match(uri) == 1)
            id = msqLiteHelper.getWritableDatabase().insert(MSQLiteHelper.ITEM_TABLE, null, values);
        else if (uriMatcher.match(uri) == 2)
            id = msqLiteHelper.getWritableDatabase().insert(MSQLiteHelper.LABEL_TABLE, null, values);
        else
            id = msqLiteHelper.getWritableDatabase().insert(MSQLiteHelper.ALL_LABEL_TABLE, null, values);

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(PATH_ITEM + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) == 1)
            msqLiteHelper.getWritableDatabase().delete(MSQLiteHelper.ITEM_TABLE, selection, selectionArgs);
        else if (uriMatcher.match(uri) == 2)
            msqLiteHelper.getWritableDatabase().delete(MSQLiteHelper.LABEL_TABLE, selection, selectionArgs);
        else
            msqLiteHelper.getWritableDatabase().delete(MSQLiteHelper.ALL_LABEL_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) == 1)
            msqLiteHelper.getWritableDatabase().update(MSQLiteHelper.ITEM_TABLE, values, selection, selectionArgs);
        else if (uriMatcher.match(uri) == 2)
            msqLiteHelper.getWritableDatabase().update(MSQLiteHelper.LABEL_TABLE, values, selection, selectionArgs);
        else
            msqLiteHelper.getWritableDatabase().update(MSQLiteHelper.ALL_LABEL_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return 0;
    }
}
