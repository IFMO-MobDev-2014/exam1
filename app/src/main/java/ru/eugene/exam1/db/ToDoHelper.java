package ru.eugene.exam1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by eugene on 1/21/15.
 */
public class ToDoHelper extends SQLiteOpenHelper {
    public static final int VERSION = 3;
    public static final String NAME = "to_do.db";

    public ToDoHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LabelSource.CREATE_TABLE);
        db.execSQL(ToDoSource.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LabelSource.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ToDoSource.TABLE_NAME);
        onCreate(db);
    }
}
