package ru.ifmo.md.exam1.database;

/**
 * Created by heat_wave on 21.01.15.
 */
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LabelsTable {

    // Database table
    public static final String TABLE_LABELS = "labels";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY = "category";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_LABELS
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CATEGORY + " text not null" + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TodoTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_LABELS);
        onCreate(database);
    }
}
