package ru.ifmo.md.exam1.database;

/**
 * Created by sergey on 21.01.15.
 */
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

public class TodoTable {

    public static final String TABLE_NAME = "TODOS_TABLE";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_SUMMARY = "summary";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PUB_DATE = "pub_date";

    private static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CATEGORY + " text, "
            + COLUMN_SUMMARY + " text,"
            + COLUMN_DESCRIPTION + " text,"
            + COLUMN_PUB_DATE + " text"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE);
        database.execSQL(addDefaultTodo("Urgent!", "Dinner!", "Have a dinner with my puppy", "11:12:13"));
        database.execSQL(addDefaultTodo("Remind me", "Sleep", "Sleep half an hour", "11:13:10"));
        database.execSQL(addDefaultTodo("Work", "Colloquium", "Write a wonderful application!", "11:14:13"));
    }

    private static String addDefaultTodo(String category, String summary, String description, String pubDate) {
        return "INSERT INTO " + TABLE_NAME + "(" +
                    COLUMN_CATEGORY + ", " +
                    COLUMN_SUMMARY + ", " +
                    COLUMN_DESCRIPTION + ", " +
                    COLUMN_PUB_DATE +
                ") VALUES (" +
                    DatabaseUtils.sqlEscapeString(category) + ", " +
                    DatabaseUtils.sqlEscapeString(summary) + ", " +
                    DatabaseUtils.sqlEscapeString(description) + ", " +
                    DatabaseUtils.sqlEscapeString(pubDate) +
                ");";
    }


    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}