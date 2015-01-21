package ru.ifmo.mobdev.exam1.db;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * @author sugakandrey
 */
public class TasksDatabase implements BaseColumns {
    public static final String TABLE_NAME = "TasksTable";

    public static final String DATE = "date";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "text";
    public static final String LABELS = "category";

    private static String DB_CREATE = "create table if not exists " + TABLE_NAME + " ( " +
                                       BaseColumns._ID + " integer primary key autoincrement, " +
                                       NAME + " string not null, " +
                                       DATE + " string not null, " +
            DESCRIPTION + " string, " +
            LABELS + " string); ";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
