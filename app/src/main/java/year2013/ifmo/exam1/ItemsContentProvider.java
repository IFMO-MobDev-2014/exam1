package year2013.ifmo.exam1;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Юлия on 21.01.2015.
 */
public class ItemsContentProvider extends ContentProvider {

    public static final String ITEMS = "item";
    public static final String LABELS_TABLE_NAME = "label";
    public static final String EXPRESSIONS_TABLE_NAME = "expression";

    public ItemsContentProvider() {}

    private ItemDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new ItemDbHelper(getContext());
        return true;
    }

    private static final int EXPRESSIONS = 1;
    private static final int EXPRESSIONS_ID = 2;
    private static final int LABELS = 3;
    private static final int LABELS_ID = 4;
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Item.AUTHORITY, Item.Expression.EXPRESSION_NAME, EXPRESSIONS);
        uriMatcher.addURI(Item.AUTHORITY, Item.Expression.EXPRESSION_NAME + "/#", EXPRESSIONS_ID);
        uriMatcher.addURI(Item.AUTHORITY, Item.Label.LABEL_NAME, LABELS);
        uriMatcher.addURI(Item.AUTHORITY, Item.Label.LABEL_NAME + "/#", LABELS_ID);
    }

    private static class ItemDbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = ITEMS + ".db";
        private static int DATABASE_VERSION = 3;

        private ItemDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String s = "CREATE TABLE " + EXPRESSIONS_TABLE_NAME + " ("
                    + Item.Expression._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Item.Expression.TITLE_NAME + " TEXT, "
                    + Item.Expression.DATE_NAME + " TEXT, "
                    + Item.Expression.LABEL_NAME + " TEXT, "
                    + Item.Expression.DESCRIPTION_NAME + " TEXT" + ");";
            sqLiteDatabase.execSQL(s);
            s = "CREATE TABLE " + LABELS_TABLE_NAME + " ("
                    + Item.Label._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Item.Label.TITLE_NAME + " TEXT, "
                    + Item.Label.NUMBER_NAME + " INTEGER" + ");";
            sqLiteDatabase.execSQL(s);
            ContentValues cv = new ContentValues();
            cv.put(Item.Label.TITLE_NAME, "funny");
            cv.put(Item.Label.NUMBER_NAME, 0);
            sqLiteDatabase.insert(LABELS_TABLE_NAME, null, cv);
            cv = new ContentValues();
            cv.put(Item.Label.TITLE_NAME, "work");
            cv.put(Item.Label.NUMBER_NAME, 0);
            sqLiteDatabase.insert(LABELS_TABLE_NAME, null, cv);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldv, int newv) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EXPRESSIONS_TABLE_NAME + ";");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LABELS_TABLE_NAME + ";");
            onCreate(sqLiteDatabase);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        int u = uriMatcher.match(uri);
        if (u != EXPRESSIONS && u != LABELS) {
            throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        // Log.d("LogsLL", ((Integer) u).toString());
        ContentValues values;
        if (initialValues != null) {
            values = initialValues;
        } else {
            values = new ContentValues();
        }

        if (u == EXPRESSIONS) {
            long rowID = dbHelper.getWritableDatabase().insert(EXPRESSIONS_TABLE_NAME, null, values);
            if (rowID > 0) {
                Uri resultUri = ContentUris.withAppendedId(Item.Expression.CONTENT_URI, rowID);
                getContext().getContentResolver().notifyChange(resultUri, null);
                return resultUri;
            }
        } else {
            long rowID = dbHelper.getWritableDatabase().insert(LABELS_TABLE_NAME, null, values);
            if (rowID > 0) {
                Uri resultUri = ContentUris.withAppendedId(Item.Expression.CONTENT_URI, rowID);
                getContext().getContentResolver().notifyChange(resultUri, null);
                return resultUri;
            }
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int match = uriMatcher.match(uri);
        int affected;

        switch (match) {
            case EXPRESSIONS:
                affected = dbHelper.getWritableDatabase().delete(EXPRESSIONS_TABLE_NAME,
                        (!TextUtils.isEmpty(selection) ?
                                " (" + selection + ')' : ""),
                        selectionArgs);
                break;
            case EXPRESSIONS_ID:
                long sourceId = ContentUris.parseId(uri);
                affected = dbHelper.getWritableDatabase().delete(EXPRESSIONS_TABLE_NAME,
                        Item.Expression._ID + "=" + sourceId
                                + (!TextUtils.isEmpty(selection) ?
                                " (" + selection + ')' : ""),
                        selectionArgs);
                break;
            case LABELS:
                affected = dbHelper.getWritableDatabase().delete(LABELS_TABLE_NAME,
                        (!TextUtils.isEmpty(selection) ?
                                " (" + selection + ')' : ""),
                        selectionArgs);
                break;
            case LABELS_ID:
                long itemId = ContentUris.parseId(uri);
                affected = dbHelper.getWritableDatabase().delete(LABELS_TABLE_NAME,
                        Item.Label._ID + "=" + itemId
                                + (!TextUtils.isEmpty(selection) ?
                                " (" + selection + ')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("unknown feed element: " +
                        uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return affected;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        String TABLE_NAME;
        Uri CONTENT_URI;
        switch (uriMatcher.match(uri)) {
            case EXPRESSIONS:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = Item.Expression.TITLE_NAME + " ASC";
                }
                TABLE_NAME = EXPRESSIONS_TABLE_NAME;
                CONTENT_URI = Item.Expression.CONTENT_URI;
                break;
            case EXPRESSIONS_ID: {
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Item.Expression._ID + " = " + id;
                } else {
                    selection = selection + " AND " + Item.Expression._ID + " = " + id;
                }
                TABLE_NAME = EXPRESSIONS_TABLE_NAME;
                CONTENT_URI = Item.Expression.CONTENT_URI;
            }
            break;
            case LABELS:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = Item.Label.TITLE_NAME + " ASC";
                }
                TABLE_NAME = LABELS_TABLE_NAME;
                CONTENT_URI = Item.Label.CONTENT_URI;
                break;
            case LABELS_ID: {
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Item.Label._ID + " = " + id;
                } else {
                    selection = selection + " AND " + Item.Label._ID + " = " + id;
                }
                TABLE_NAME = LABELS_TABLE_NAME;
                CONTENT_URI = Item.Label.CONTENT_URI;
            }
            break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        Cursor cursor = dbHelper.getWritableDatabase().query(TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), CONTENT_URI);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int affected;

        switch (uriMatcher.match(uri)) {
            case EXPRESSIONS:
                affected = dbHelper.getWritableDatabase().update(EXPRESSIONS_TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case EXPRESSIONS_ID:
                String feedId = uri.getPathSegments().get(1);
                affected = dbHelper.getWritableDatabase().update(EXPRESSIONS_TABLE_NAME, values,
                        Item.Label._ID + "=" + feedId
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            case LABELS:
                affected = dbHelper.getWritableDatabase().update(LABELS_TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case LABELS_ID:
                String postId = uri.getPathSegments().get(1);
                affected = dbHelper.getWritableDatabase().update(LABELS_TABLE_NAME, values,
                        Item.Label._ID + "=" + postId
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return affected;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case EXPRESSIONS:
                return Item.Expression.CONTENT_TYPE;
            case EXPRESSIONS_ID:
                return Item.Expression.CONTENT_ITEM_TYPE;
            case LABELS:
                return Item.Label.CONTENT_TYPE;
            case LABELS_ID:
                return Item.Label.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown type: " + uri);
        }
    }

}
