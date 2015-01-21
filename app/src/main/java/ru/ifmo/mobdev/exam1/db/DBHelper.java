package ru.ifmo.mobdev.exam1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author sugakandrey
 */
public class DBHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ImagesDB";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        TasksDatabase.onCreate(db);
        CategoriesDatabase.onCreate(db);
        ContentValues cv = new ContentValues();
        cv.put(CategoriesDatabase.NAME, "Работа");
        db.insert(CategoriesDatabase.TABLE_NAME, null, cv);
        cv.clear();
        cv.put(CategoriesDatabase.NAME, "Учеба");
        db.insert(CategoriesDatabase.TABLE_NAME, null, cv);
        cv.clear();
        cv.put(CategoriesDatabase.NAME, "Другое");
        db.insert(CategoriesDatabase.TABLE_NAME, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TasksDatabase.onUpgrade(db, oldVersion, newVersion);
        CategoriesDatabase.onUpgrade(db, oldVersion, newVersion);
    }
}
