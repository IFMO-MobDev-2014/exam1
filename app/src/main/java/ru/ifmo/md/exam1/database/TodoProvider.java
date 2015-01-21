package ru.ifmo.md.exam1.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class TodoProvider extends ContentProvider {

    private TodoDatabase databaseHelper;

    private static final int TODOS = 10;
    private static final int TODO_ID = 20;
    private static final int LABELS = 30;
    private static final int LABEL_ID = 40;

    private static final String CONTENT_AUTHORITY = "ru.ifmo.md.exam1.todos.contentprovider";

    private static final String PATH_TODOS = "todos";
    private static final String PATH_LABELS = "labels";

    public static final Uri CONTENT_URI_TODOS = Uri.parse("content://" + CONTENT_AUTHORITY + "/" + PATH_TODOS);
    public static final Uri CONTENT_URI_LABELS = Uri.parse("content://" + CONTENT_AUTHORITY + "/" + PATH_LABELS);

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_TODOS, TODOS);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_TODOS + "/#", TODO_ID);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_LABELS, LABELS);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_LABELS + "/#", LABEL_ID);
    }

    public static Uri buildTodoUri(String todoUri) {
        return CONTENT_URI_TODOS.buildUpon().appendPath(todoUri).build();
    }

    public static Uri buildLabelUri(String labelUri) {
        return CONTENT_URI_LABELS.buildUpon().appendPath(labelUri).build();
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new TodoDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = uriMatcher.match(uri);
        if (uriType == TODOS || uriType == TODO_ID)
            queryBuilder.setTables(TodoTable.TABLE_NAME);
        else
            queryBuilder.setTables(LabelsTable.TABLE_NAME);
        switch (uriType) {
            case TODOS:
                break;
            case TODO_ID:
                queryBuilder.appendWhere(TodoTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case LABELS:
                break;
            case LABEL_ID:
                queryBuilder.appendWhere(LabelsTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    public String getType(Uri uri) {
        return null;
    }

    public Uri insert(Uri uri, ContentValues values) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case TODOS:
                id = db.insert(TodoTable.TABLE_NAME, null, values);
                break;
            case LABELS:
                id = db.insert(LabelsTable.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.withAppendedPath(uri, String.valueOf(id));
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case TODOS:
                rowsDeleted = db.delete(TodoTable.TABLE_NAME, selection, selectionArgs);
                break;
            case TODO_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(TodoTable.TABLE_NAME, TodoTable.COLUMN_ID + "=" + id, null);
                } else {
                    rowsDeleted = db.delete(TodoTable.TABLE_NAME, TodoTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case LABELS:
                rowsDeleted = db.delete(LabelsTable.TABLE_NAME, selection, selectionArgs);
                break;
            case LABEL_ID:
                String id2 = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(LabelsTable.TABLE_NAME, LabelsTable.COLUMN_ID + "=" + id2, null);
                } else {
                    rowsDeleted = db.delete(LabelsTable.TABLE_NAME, LabelsTable.COLUMN_ID + "=" + id2 + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case TODOS:
                rowsUpdated = db.update(TodoTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TODO_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(TodoTable.TABLE_NAME, values, TodoTable.COLUMN_ID + "=" + id, null);
                } else {
                    rowsUpdated = db.update(TodoTable.TABLE_NAME, values, TodoTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case LABELS:
                rowsUpdated = db.update(LabelsTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            case LABEL_ID:
                String id2 = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(LabelsTable.TABLE_NAME, values, LabelsTable.COLUMN_ID + "=" + id2, null);
                } else {
                    rowsUpdated = db.update(LabelsTable.TABLE_NAME, values, LabelsTable.COLUMN_ID + "=" + id2 + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

}