package ru.ifmo.mobdev.exam1.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * @author sugakandrey
 */
public class MyContentProvider extends ContentProvider {
    private static String TASKS_TABLE = "TasksTable";
    private static String CATEGORIES_TABLE = "CategoriesTable";

    private static String AUTHORITY = "ru.ifmo.mobdev.exam1";

    private DBHelper helper;

    private static final int TASKS = 1;
    private static final int CATEGORIES = 2;

    private static final String PATH_TASKS = "TasksTable";
    private static final String PATH_LABLES = "CategoriesTable";

    public static final Uri CONTENT_URI_TASKS =
            Uri.parse("content://" + AUTHORITY + "/" + PATH_TASKS);

    public static final Uri CONTENT_URI_LABLES =
            Uri.parse("content://" + AUTHORITY + "/" + PATH_LABLES);


    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, PATH_TASKS, TASKS);
        uriMatcher.addURI(AUTHORITY, PATH_LABLES, CATEGORIES);
    }

    public boolean onCreate() {
        helper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = helper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case TASKS:
                queryBuilder.setTables(TASKS_TABLE);
                break;
            case CATEGORIES:
                queryBuilder.setTables(CATEGORIES_TABLE);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long id;
        switch (uriMatcher.match(uri)) {
            case TASKS:
                id = db.insert(TasksDatabase.TABLE_NAME, null, values);
                break;
            case CATEGORIES:
                id = db.insert(CategoriesDatabase.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.withAppendedPath(uri, String.valueOf(id));
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int removed;
        switch (uriMatcher.match(uri)) {
            case TASKS:
                removed = db.delete(TasksDatabase.TABLE_NAME, selection, selectionArgs);
                break;
            case CATEGORIES:
                removed = db.delete(CategoriesDatabase.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return removed;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int updated;
        switch (uriMatcher.match(uri)) {
            case TASKS:
                updated = db.update(TasksDatabase.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CATEGORIES:
                updated = db.update(CategoriesDatabase.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updated;
    }
}
