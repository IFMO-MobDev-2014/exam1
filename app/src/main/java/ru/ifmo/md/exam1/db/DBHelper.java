package ru.ifmo.md.exam1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.ifmo.md.exam1.db.model.CategoryTable;
import ru.ifmo.md.exam1.db.model.CategoryToTaskTable;
import ru.ifmo.md.exam1.db.model.TaskTable;

/**
 * Created by Kirill on 21.01.2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TODOListDB";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
        CategoryTable.onCreate(db);
        TaskTable.onCreate(db);
        CategoryToTaskTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        CategoryTable.onUpgrade(db, oldVersion, newVersion);
        TaskTable.onUpgrade(db, oldVersion, newVersion);
        CategoryToTaskTable.onUpgrade(db, oldVersion, newVersion);
    }
}