package ru.ifmo.md.exam1;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

public class TodoProvider extends ContentProvider {

    static final String PROVIDER_NAME = "ru.ifmo.md.exam1.TodoProvider";
    static final String TODO_ADDRESS = "content://" + PROVIDER_NAME + "/todo";
    static final String TAGS_ADDRESS = "content://" + PROVIDER_NAME + "/tags";
    static final Uri TODO_URI = Uri.parse(TODO_ADDRESS);
    static final Uri TAGS_URI = Uri.parse(TAGS_ADDRESS);

    static final String _ID = "_id";
    static final String TITLE = "title";
    static final String DATE = "date";
    static final String NAME = "name";
    static final String NOTE_ID = "note_id";
    static final String DESC = "desc";

    private static HashMap<String, String> WEATHER_PROJECTION_MAP, CITIES_PROJECTION_MAP;

    static final int TODO = 1;
    static final int TODO_ID = 2;
    static final int TAGS = 3;
    static final int TAGS_ID = 4;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "todo", TODO);
        uriMatcher.addURI(PROVIDER_NAME, "todo/#", TODO_ID);
        uriMatcher.addURI(PROVIDER_NAME, "tags", TAGS);
        uriMatcher.addURI(PROVIDER_NAME, "tags/#", TAGS_ID);
    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Weather";
    static final String TODO_TABLE_NAME = "todo";
    static final String TAGS_TABLE_NAME = "tags";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_TODO_TABLE =
            " CREATE TABLE " + TODO_TABLE_NAME +
                    " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TITLE + " TEXT NOT NULL, " +
                    DESC + " TEXT, " +
                    DATE + " TEXT NOT NULL);";
    static final String CREATE_TAG_TABLE =
            " CREATE TABLE " + TAGS_TABLE_NAME +
                    " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME + " TEXT NOT NULL, " +
                    NOTE_ID + " INTEGER);";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TODO_TABLE);
            db.execSQL(CREATE_TAG_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TAGS_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return (db != null);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = -1;
        switch (uriMatcher.match(uri)) {
            case TODO:
                rowID = db.insert(TODO_TABLE_NAME, "", values);
                break;
            case TAGS:
                rowID = db.insert(TAGS_TABLE_NAME, "", values);
                break;
        }

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(uri, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case TODO:
                qb.setTables(TODO_TABLE_NAME);
                qb.setProjectionMap(WEATHER_PROJECTION_MAP);
                break;
            case TODO_ID:
                qb.setTables(TODO_TABLE_NAME);
                qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                break;
            case TAGS:
                qb.setTables(TAGS_TABLE_NAME);
                qb.setProjectionMap(WEATHER_PROJECTION_MAP);
                break;
            case TAGS_ID:
                qb.setTables(TAGS_TABLE_NAME);
                qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder.equals("")){
            sortOrder = _ID + " DESC";
        }
        Cursor c = qb.query(db,	projection,	selection, selectionArgs,
                null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case TODO:
                count = db.delete(TODO_TABLE_NAME, selection, selectionArgs);
                break;
            case TODO_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(TODO_TABLE_NAME, _ID +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            case TAGS:
                count = db.delete(TAGS_TABLE_NAME, selection, selectionArgs);
                break;
            case TAGS_ID:
                id = uri.getPathSegments().get(1);
                count = db.delete(TAGS_TABLE_NAME, _ID +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case TODO:
                count = db.update(TODO_TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case TODO_ID:
                count = db.update(TODO_TABLE_NAME, values, _ID +
                        " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            case TAGS:
                count = db.update(TAGS_TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case TAGS_ID:
                count = db.update(TAGS_TABLE_NAME, values, _ID +
                        " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}