package ru.ifmo.md.exam1.database;

import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sergey on 15.11.14.
 */
public class LabelsTable {

    public static final String TABLE_NAME = "LABELS_TABLE";
    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_DESCRIPTION = "description";

    private static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DESCRIPTION + " TEXT NOT NULL"
            + ");";

    private static String addDefaultLabel(String description) {
        return "INSERT INTO " + TABLE_NAME + "(" +
                COLUMN_DESCRIPTION +
                ") VALUES (" +
                DatabaseUtils.sqlEscapeString(description) + ");";
    }


    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE);
        database.execSQL(addDefaultLabel("Urgent!"));
        database.execSQL(addDefaultLabel("Remind me"));
        database.execSQL(addDefaultLabel("Work"));
        database.execSQL(addDefaultLabel("Learning"));
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

}
