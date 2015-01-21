package ru.ifmo.mobdev.exam1.db;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * @author sugakandrey
 */
public class CategoriesDatabase implements BaseColumns {
    public static final String TABLE_NAME = "CategoriesTable";

    public static final String NAME = "name";

    private static String DB_CREATE = "create table if not exists " + TABLE_NAME + " ( " +
            BaseColumns._ID + " integer primary key autoincrement, " +
            NAME + " string not null); ";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
