package ru.ifmo.md.exam1;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Svet on 16.01.2015.
 */
public class MyContentProvider extends ContentProvider implements BaseColumns {

    //constants
    static final String DB_NAME = "mydatabase";
    static final int DB_VERSION = 1;

    //tables
    public static final String TASK_TABLE = "task_table";
    public static final String LABELS_TABLE = "labels_table";

    //colums tasks
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String DESCRIPTION = "description";
    public static final String LABELS = "labels";

    //colums labels
    public static final String NAME = "label_name";

    //create table script
    static final String DB_TASKS = "create table " + TASK_TABLE + "(" +
            _ID + " integer primary key autoincrement, " +
            TITLE + " text, " +
            DATE + " text, " +
            DESCRIPTION + " text, " +
            LABELS + " text" +
            ");";

    static final String DB_LABELS = "create table " + LABELS_TABLE + "(" +
            _ID + " integer primary key autoincrement, " +
            NAME + " text " +
            ");";

    //Uri
    //autority
    static final String AUTHORITY = "ru.ifmo.md.exam1";

    //path
    static final String DATA_PATH = "images";

    //overall uri
    public static final Uri DATA_CONTENT_URI = Uri.parse("content://" +
            AUTHORITY + "/" + DATA_PATH);

    public static final String TABLE1 = "table1";
    public static final String TABLE2 = "table2";
    public static final Uri TABLE_1 = Uri.parse("content://" + AUTHORITY  + "/" + TABLE1);
    public static final Uri TABLE_2 = Uri.parse("content://" + AUTHORITY + "/" + TABLE2);

    //types
    //set of items
    static final String DATA_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." +
            AUTHORITY + "." + DATA_PATH;

    //single item
    static final String DATA_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." +
            AUTHORITY + "." + DATA_PATH;

    //UriMatcher
    // overall Uri
    static final int URI_DATA = 1;

    // Uri with fixed ID
    static final int URI_DATA_ID = 2;

    static final int URI_TABLE_1 = 3;

    static final int URI_TABLE_2 = 4;

    //UriMatcher description and creation
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, DATA_PATH, URI_DATA);
        uriMatcher.addURI(AUTHORITY, DATA_PATH + "/#", URI_DATA_ID);
        uriMatcher.addURI(AUTHORITY, TABLE1, URI_TABLE_1);
        uriMatcher.addURI(AUTHORITY, TABLE2, URI_TABLE_2);
    }

    DBHelper dbHelper;
    SQLiteDatabase sql;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //check Urk
        String tableName = "";
        switch (uriMatcher.match(uri)) {
            case URI_TABLE_1: {
                tableName = TASK_TABLE;
                break;
            }
            case URI_TABLE_2: {
                tableName = LABELS_TABLE;
                break;
            }
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri.toString());
        }

        sql = dbHelper.getWritableDatabase();

        Cursor cursor = sql.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);

        //set notification flag on cursor
        cursor.setNotificationUri(getContext().getContentResolver(), DATA_CONTENT_URI);

        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        Log.i("MESSAGE", "Insert " + uri.toString());

        sql = dbHelper.getWritableDatabase();

        String tableName = "";
        switch (uriMatcher.match(uri)) {
            case URI_TABLE_1: {
                tableName = TASK_TABLE;
                break;
            }
            case URI_TABLE_2: {
                tableName = LABELS_TABLE;
                break;
            }
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri.toString());
        }

        long rowID = sql.insert(tableName, null, values);

        Uri resultUri = ContentUris.withAppendedId(DATA_CONTENT_URI, rowID);
        //notify ContentResolver about changes
        getContext().getContentResolver().notifyChange(resultUri, null);

        return resultUri;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_DATA:
                return DATA_CONTENT_TYPE;
            case URI_DATA_ID:
                return DATA_CONTENT_ITEM_TYPE;
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        selection = makeRoutine(uri, selection);

        sql = dbHelper.getWritableDatabase();
        String tableName = "";
        switch (uriMatcher.match(uri)) {
            case URI_TABLE_1: {
                tableName = TASK_TABLE;
                break;
            }
            case URI_TABLE_2: {
                tableName = LABELS_TABLE;
                break;
            }
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri.toString());
        }

        int count = sql.delete(tableName, selection, selectionArgs);
        //notify
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
//        selection = makeRoutine(uri, selection);

        sql = dbHelper.getWritableDatabase();
        String tableName = "";
        switch (uriMatcher.match(uri)) {
            case URI_TABLE_1: {
                tableName = TASK_TABLE;
                break;
            }
            case URI_TABLE_2: {
                tableName = LABELS_TABLE;
                break;
            }
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri.toString());
        }
        int count = sql.update(tableName, contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private String makeRoutine(Uri uri, String selection) {
        switch (uriMatcher.match(uri)) {
            case URI_DATA: {
                break;
            }
            case URI_DATA_ID: {
                String id = uri.getLastPathSegment();
                if(selection.isEmpty()) {
                    selection = _ID + " = " + id;
                } else {
                    selection = selection + " AND " + _ID + " = " + id;
                }
                break;
            }
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri.toString());
        }
        return selection;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(DB_TASKS);
            sqLiteDatabase.execSQL(DB_LABELS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        }
    }
}