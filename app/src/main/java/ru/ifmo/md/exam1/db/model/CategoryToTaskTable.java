package ru.ifmo.md.exam1.db.model;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by Kirill on 21.01.2015.
 */
public class CategoryToTaskTable implements BaseColumns {
    public static final String TABLE_NAME = "CategoryToTaskTable";

    public static final String CATEGORY_ID_COLUMN = "categoryId";
    public static final String TASK_ID_COLUMN = "taskId";

    private static String DB_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    BaseColumns._ID + " integer PRIMARY KEY autoincrement, " +
                    CATEGORY_ID_COLUMN + " integer, " +
                    TASK_ID_COLUMN + " integer " + "); ";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

