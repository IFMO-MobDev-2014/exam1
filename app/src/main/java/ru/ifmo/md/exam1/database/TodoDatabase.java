package ru.ifmo.md.exam1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sergey on 10.11.14.
 */
public class TodoDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME = "todo.db";
    public static final int DB_VERSION = 1;

    public TodoDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        TodoTable.onCreate(sqLiteDatabase);
        LabelsTable.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        TodoTable.onUpgrade(sqLiteDatabase, i, i2);
        LabelsTable.onUpgrade(sqLiteDatabase, i, i2);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
