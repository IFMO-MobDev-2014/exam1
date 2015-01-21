package ru.ya.exam.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MSQLiteHelper extends SQLiteOpenHelper {
    public static final String ITEM_TABLE = "ITEM_T";
    public static final String LABEL_TABLE = "LABEL_T";
    public static final String ALL_LABEL_TABLE = "ALL_LABEL_T";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "TITLEEE";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_DATE = "DATE";

    public static final String COLUMN_LABEL = "LABEL";
    public static final String COLUMN_ITEM_ID = "ITEM_ID";

    public MSQLiteHelper(Context context ) {
        super(context, "myBase", null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ITEM_TABLE + " ("
                + COLUMN_DESCRIPTION + " text, "
                + COLUMN_TITLE + " text, "
                + COLUMN_DATE + " text, "
                + COLUMN_ID + " integer primary key autoincrement) " );

        db.execSQL("CREATE TABLE " + LABEL_TABLE + " ("
                + COLUMN_LABEL + " text, "
                + COLUMN_ITEM_ID + " integer, "
                + COLUMN_ID + " integer primary key autoincrement) " );

        db.execSQL("CREATE TABLE " + ALL_LABEL_TABLE + " ("
                + COLUMN_LABEL + " text, "
                + COLUMN_ID + " integer primary key autoincrement) " );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE " + ITEM_TABLE);
            db.execSQL("DROP TABLE " + LABEL_TABLE);
            db.execSQL("DROP TABLE " + ALL_LABEL_TABLE);
            onCreate(db);
        }
    }
}


