package ru.ifmo.md.exam1;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class MyContentProvider extends ContentProvider {
    public static final String DATABASE_NAME =  "base.db";
    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TASK_TEXT = "text";
    public static final String COLUMN_TASK_MARK = "marks";
    public static final String COLUMN_TASK_DATE = "date";
    public static final int DATABASE_VERSION = 1;
    public static final String AUTHORITY = "ru.ifmo.md.exam1";


    public static final String CREATE_TABLE_COURCES = "CREATE TABLE "
            + TABLE_TASKS + " ( "
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TASK_TEXT + " TEXT NOT NULL, "
            + COLUMN_TASK_DATE + " TEXT NOT NULL, "
            + COLUMN_TASK_MARK + " TEXT NOT NULL "
            +");";


    public static final Uri TABLE_TASKS_URI = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_TASKS);

    static final int TASKS = 1;
    static final int TASKS_ID = 3;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, TABLE_TASKS + "/#", TASKS_ID);
        uriMatcher.addURI(AUTHORITY, TABLE_TASKS, TASKS);
    }

    MySQLiteHelper dbHelper;


    @Override
    public boolean onCreate() {
        dbHelper = new MySQLiteHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings2, String s2) {
        SQLiteQueryBuilder sqB = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case TASKS:
                sqB.setTables(TABLE_TASKS);
                break;
            case TASKS_ID:
                sqB.setTables(TABLE_TASKS);
                sqB.appendWhere(COLUMN_TASK_TEXT +"="+uri.getLastPathSegment());
                break;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = sqB.query(db, strings, s, strings2, null, null, s2);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    @Override
    public String getType(Uri uri) {
        return "";
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase sqlQB = dbHelper.getWritableDatabase();
        long insertedID=1;
        switch (uriMatcher.match(uri)) {
            case TASKS:
                insertedID = sqlQB.insert(TABLE_TASKS, null, contentValues);
                break;
            case TASKS_ID:
                contentValues.put(COLUMN_TASK_TEXT, uri.getLastPathSegment());
                insertedID = sqlQB.insert(TABLE_TASKS, null, contentValues);
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.withAppendedPath(uri, Long.toString(insertedID));
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase sqlQB = dbHelper.getWritableDatabase();
        int result = 0;
        switch (uriMatcher.match(uri)) {
            case TASKS_ID:
                String ending = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)) {
                    result = sqlQB.delete(TABLE_TASKS, COLUMN_ID + " = "+ ending, null);
                }
                else {
                    result = sqlQB.delete(TABLE_TASKS, COLUMN_ID + " = "+ ending + " and " + s, strings);
                }
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase sqlQB = dbHelper.getWritableDatabase();
        int result = 0;
        String ending;
        switch (uriMatcher.match(uri)) {
            case TASKS_ID:
                ending = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)) {
                    result = sqlQB.update(TABLE_TASKS, contentValues, COLUMN_ID + " = "+ ending, null);
                }
                else {
                    result = sqlQB.update(TABLE_TASKS, contentValues, COLUMN_ID + " = "+ ending + " and " + s, strings);
                }
                break;
            case TASKS:
                if (TextUtils.isEmpty(s)) {
                    result = sqlQB.update(TABLE_TASKS, contentValues, null, null);
                }
                else {
                    result = sqlQB.update(TABLE_TASKS, contentValues, s, strings);
                }
                break;

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    public class MySQLiteHelper extends SQLiteOpenHelper {

        public MySQLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(CREATE_TABLE_COURCES);
            Log.d("DATABASE", "added");
            database.execSQL("insert into " + TABLE_TASKS +" ("+ COLUMN_TASK_TEXT +", " + COLUMN_TASK_DATE +", " + COLUMN_TASK_MARK + ") values (?, ?, ?)", new String[]{"Go to the gym", "23.12.2014", "0 1"});
            database.execSQL("insert into " + TABLE_TASKS +" ("+ COLUMN_TASK_TEXT +", " + COLUMN_TASK_DATE +", " + COLUMN_TASK_MARK + ") values (?, ?, ?)", new String[]{"Pass the exam", "24.12.2014", "1 2"});
            database.execSQL("insert into " + TABLE_TASKS +" ("+ COLUMN_TASK_TEXT +", " + COLUMN_TASK_DATE +", " + COLUMN_TASK_MARK + ") values (?, ?, ?)", new String[]{"Improve english", "25.12.2014", "0 2"});
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(MySQLiteHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            onCreate(db);
        }
    }

}