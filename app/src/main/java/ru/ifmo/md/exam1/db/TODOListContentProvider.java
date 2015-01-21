package ru.ifmo.md.exam1.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import ru.ifmo.md.exam1.db.model.CategoryTable;
import ru.ifmo.md.exam1.db.model.CategoryToTaskTable;
import ru.ifmo.md.exam1.db.model.TaskTable;

/**
 * Created by Kirill on 21.01.2015.
 */
public class TODOListContentProvider extends ContentProvider {

    private static final String TASK_TABLE = TaskTable.TABLE_NAME;
    private static final String CATEGORY_TABLE = CategoryTable.TABLE_NAME;
    private static final String CATEGORY_TO_TASK_TABLE = CategoryToTaskTable.TABLE_NAME;

    private DBHelper helper;

    private static final int SINGLE_TASK = 1;
    private static final int SINGLE_CATEGORY = 2;
    private static final int ALL_CATEGORIES = 3;
    private static final int ALL_TASKS = 4;
    private static final int SINGLE_CATEGORY_TO_TASK = 5;
    private static final int ALL_CATEGORIES_TO_TASKS = 6;
    
    private static final String AUTHORITY = "ru.ifmo.md.exam1";
    private static final String PATH_TASK = "Tasks";
    private static final String PATH_CATEGORY = "Category";
    private static final String PATH_CATEGORY_TO_TASK = "CategoryToTask";

    public static final Uri CONTENT_URI_CATEGORY =
            Uri.parse("content://" + AUTHORITY + "/" + PATH_CATEGORY);
    public static final Uri CONTENT_URI_TASK =
            Uri.parse("content://" + AUTHORITY + "/" + PATH_TASK);
    public static final Uri CONTENT_URI_CATEGORY_TO_TASK =
            Uri.parse("content://" + AUTHORITY + "/" + PATH_CATEGORY_TO_TASK);

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, PATH_CATEGORY, ALL_CATEGORIES);
        uriMatcher.addURI(AUTHORITY, PATH_TASK, ALL_TASKS);
        uriMatcher.addURI(AUTHORITY, PATH_CATEGORY_TO_TASK, ALL_CATEGORIES_TO_TASKS);
        uriMatcher.addURI(AUTHORITY, PATH_TASK + "/#", SINGLE_TASK);
        uriMatcher.addURI(AUTHORITY, PATH_CATEGORY + "/#", SINGLE_CATEGORY);
        uriMatcher.addURI(AUTHORITY, PATH_CATEGORY_TO_TASK + "/#", SINGLE_CATEGORY_TO_TASK);
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
            case ALL_CATEGORIES:
                queryBuilder.setTables(CATEGORY_TABLE);
                break;
            case ALL_TASKS:
                queryBuilder.setTables(TASK_TABLE);
                break;
            case ALL_CATEGORIES_TO_TASKS:
                queryBuilder.setTables(CATEGORY_TO_TASK_TABLE);
                break;
            case SINGLE_TASK:
                queryBuilder.setTables(TASK_TABLE);
                queryBuilder.appendWhere(TaskTable._ID + "=" + uri.getLastPathSegment());
                break;
            case SINGLE_CATEGORY:
                queryBuilder.setTables(CATEGORY_TABLE);
                queryBuilder.appendWhere(CategoryTable._ID + "=" + uri.getLastPathSegment());
                break;
            case SINGLE_CATEGORY_TO_TASK:
                queryBuilder.setTables(CATEGORY_TO_TASK_TABLE);
                queryBuilder.appendWhere(CategoryToTaskTable._ID + "=" + uri.getLastPathSegment());
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
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long id;
        switch (uriMatcher.match(uri)) {
            case ALL_TASKS:
                id = db.insert(TaskTable.TABLE_NAME, null, contentValues);
                break;
            case ALL_CATEGORIES:
                id = db.insert(CategoryTable.TABLE_NAME, null, contentValues);
                break;
            case ALL_CATEGORIES_TO_TASKS:
                id = db.insert(CategoryToTaskTable.TABLE_NAME, null, contentValues);
                break;
            case SINGLE_TASK:
                contentValues.put(TaskTable._ID, uri.getLastPathSegment());
                id = db.insert(TaskTable.TABLE_NAME, null, contentValues);
                break;
            case SINGLE_CATEGORY:
                contentValues.put(CategoryTable._ID, uri.getLastPathSegment());
                id = db.insert(CategoryTable.TABLE_NAME, null, contentValues);
                break;
            case SINGLE_CATEGORY_TO_TASK:
                contentValues.put(CategoryToTaskTable._ID, uri.getLastPathSegment());
                id = db.insert(CategoryToTaskTable.TABLE_NAME, null, contentValues);
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
        String id = "";
        switch (uriMatcher.match(uri)) {
            case ALL_CATEGORIES:
                removed = db.delete(CategoryTable.TABLE_NAME, selection, selectionArgs);
                break;
            case ALL_TASKS:
                removed = db.delete(TaskTable.TABLE_NAME, selection, selectionArgs);
                break;
            case ALL_CATEGORIES_TO_TASKS:
                removed = db.delete(CategoryToTaskTable.TABLE_NAME, selection, selectionArgs);
                break;
            case SINGLE_TASK:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    removed = db.delete(TaskTable.TABLE_NAME, TaskTable._ID + "=" + id, selectionArgs);
                } else {
                    removed = db.delete(TaskTable.TABLE_NAME, TaskTable._ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case SINGLE_CATEGORY:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    removed = db.delete(CategoryTable.TABLE_NAME, CategoryTable._ID + "=" + id, selectionArgs);
                } else {
                    removed = db.delete(CategoryTable.TABLE_NAME, CategoryTable._ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case SINGLE_CATEGORY_TO_TASK:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    removed = db.delete(CategoryToTaskTable.TABLE_NAME, CategoryToTaskTable._ID + "=" + id, selectionArgs);
                } else {
                    removed = db.delete(CategoryToTaskTable.TABLE_NAME, CategoryToTaskTable._ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return removed;
    }
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int updated;
        String id = "";
        switch (uriMatcher.match(uri)) {
            case ALL_TASKS:
                updated = db.update(TaskTable.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case ALL_CATEGORIES:
                updated = db.update(CategoryTable.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case ALL_CATEGORIES_TO_TASKS:
                updated = db.update(CategoryToTaskTable.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case SINGLE_TASK:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    updated = db.update(TaskTable.TABLE_NAME, contentValues, TaskTable._ID + "=" + id, selectionArgs);
                } else {
                    updated = db.update(TaskTable.TABLE_NAME, contentValues, TaskTable._ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case SINGLE_CATEGORY:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    updated = db.update(CategoryTable.TABLE_NAME, contentValues, CategoryTable._ID + "=" + id, selectionArgs);
                } else {
                    updated = db.update(CategoryTable.TABLE_NAME, contentValues, CategoryTable._ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case SINGLE_CATEGORY_TO_TASK:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    updated = db.update(CategoryToTaskTable.TABLE_NAME, contentValues, CategoryToTaskTable._ID + "=" + id, selectionArgs);
                } else {
                    updated = db.update(CategoryToTaskTable.TABLE_NAME, contentValues, CategoryToTaskTable._ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updated;
    }

}