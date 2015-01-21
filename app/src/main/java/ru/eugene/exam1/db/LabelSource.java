package ru.eugene.exam1.db;

/**
 * Created by eugene on 1/21/15.
 */
public class LabelSource {
    public static final String TABLE_NAME = "label";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
            ", " + COLUMN_NAME + " TEXT UNIQUE NOT NULL);";

}
