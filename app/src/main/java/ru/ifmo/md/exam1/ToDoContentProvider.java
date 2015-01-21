package ru.ifmo.md.exam1;

/**
 * Created by delf on 21.01.15.
 */


import android.content.ContentProvider;
        import android.content.ContentUris;
        import android.content.ContentValues;
        import android.content.Context;
        import android.content.UriMatcher;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.net.Uri;
        import android.text.TextUtils;
        import android.util.Log;

public class ToDoContentProvider extends ContentProvider {
    final String LOG_TAG = "myLogs";
    
    static final String DB_NAME = "toDodbDelf";
    static final int DB_VERSION = 1;

    static final String MAIN_TABLE = "task";



    static final String MAIN_TABLE_ID = "_id";
    static final String MAIN_TABLE_NAME = "name";
    static final String MAIN_TABLE_FULL_TEXT = "full_text";
    static final String MAIN_TABLE_ADD_TIME = "time";
    static final String MAIN_TABLE_WORK = "work";
    static final String MAIN_TABLE_HOME = "home";
    static final String MAIN_TABLE_BUG = "bug";


    static final String DB_CREATE = "create table " + MAIN_TABLE + "("
            + MAIN_TABLE_ID + " integer primary key autoincrement, "
            + MAIN_TABLE_NAME + " text, "
            + MAIN_TABLE_FULL_TEXT + " text, "
            + MAIN_TABLE_ADD_TIME + " text DEFAULT (date('now')),"
            + MAIN_TABLE_WORK + " INTEGER DEFAULT 0,"
            + MAIN_TABLE_HOME + " INTEGER DEFAULT 0,"
            + MAIN_TABLE_BUG + " INTEGER DEFAULT 0" +
            ");";

    // // Uri
    // authority
    static final String AUTHORITY = "ru.ifmo.md.exam1";

    // path
    static final String toDo_PATH = "tasks";

    public static final Uri CONTACT_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + toDo_PATH);


    static final String CONTACT_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + toDo_PATH;

    static final String CONTACT_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + toDo_PATH;

    static final int URI_TO_DO= 1;

    static final int URI_TO_DO_ID = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, toDo_PATH, URI_TO_DO);
        uriMatcher.addURI(AUTHORITY, toDo_PATH + "/#", URI_TO_DO_ID);
    }

    DBHelper dbHelper;
    SQLiteDatabase db;

    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        dbHelper = new DBHelper(getContext());
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_TO_DO:
                Log.d(LOG_TAG, "URI_TO_DO");
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = MAIN_TABLE_NAME + " ASC";
                }
                break;
            case URI_TO_DO_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_TO_DO_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = MAIN_TABLE_ID + " = " + id;
                } else {
                    selection = selection + " AND " + MAIN_TABLE_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(MAIN_TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(),
                CONTACT_CONTENT_URI);
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());
        if (uriMatcher.match(uri) != URI_TO_DO)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = dbHelper.getWritableDatabase();

        long rowID = db.insert(MAIN_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(CONTACT_CONTENT_URI, rowID);

        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_TO_DO:
                Log.d(LOG_TAG, "URI_TO_DO");
                break;
            case URI_TO_DO_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_TO_DO_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = MAIN_TABLE_ID + " = " + id;
                } else {
                    selection = selection + " AND " + MAIN_TABLE_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(MAIN_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(LOG_TAG, "update, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_TO_DO:
                Log.d(LOG_TAG, "URI_TO_DO");

                break;
            case URI_TO_DO_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_TO_DO_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = MAIN_TABLE_ID + " = " + id;
                } else {
                    selection = selection + " AND " + MAIN_TABLE_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(MAIN_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_TO_DO:
                return CONTACT_CONTENT_TYPE;
            case URI_TO_DO_ID:
                return CONTACT_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
            ContentValues cv = new ContentValues();
            for (int i = 1; i <= 3; i++) {
                cv.put(MAIN_TABLE_NAME, "cococo");
                cv.put(MAIN_TABLE_FULL_TEXT, "cocococo");
                //cv.put(MAIN_TABLE_ADD_TIME,"");
                db.insert(MAIN_TABLE, null, cv);
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
