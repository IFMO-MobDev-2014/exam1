package ru.eugene.exam1.db;

/**
 * Created by eugene on 1/21/15.
 */
public class ToDoSource {
    public static final String TABLE_NAME = "todo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LABELS = "labels";
    public static final String COLUMN_DATA = "data";
    public static final String COLUMN_DESCRIPTION = "description";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( " +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
            ", " + COLUMN_NAME + " TEXT NOT NULL" +
            ", " + COLUMN_LABELS + " TEXT" +
            ", " + COLUMN_DATA + " integer NOT NULL unique" +
            ", " + COLUMN_DESCRIPTION + " TEXT);";
}
