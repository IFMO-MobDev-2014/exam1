package ru.ifmo.md.exam1.database;

/**
 * Created by heat_wave on 21.01.15.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LabelDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "labelstable.db";
    private static final int DATABASE_VERSION = 1;

    public LabelDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        LabelsTable.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        LabelsTable.onUpgrade(database, oldVersion, newVersion);
    }

}