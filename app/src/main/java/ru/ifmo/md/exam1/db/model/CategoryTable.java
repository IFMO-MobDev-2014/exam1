package ru.ifmo.md.exam1.db.model;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by Kirill on 21.01.2015.
 */
public class CategoryTable implements BaseColumns {
    public static final String TABLE_NAME = "CategoryTable";

    public static final String NAME_COLUMN = "name";
    public static final String COUNT_COLUMN = "count";

    private static String DB_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    BaseColumns._ID + " integer PRIMARY KEY autoincrement, " +
                    NAME_COLUMN + " text not null, " +
                    COUNT_COLUMN + " integer " + "); ";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
