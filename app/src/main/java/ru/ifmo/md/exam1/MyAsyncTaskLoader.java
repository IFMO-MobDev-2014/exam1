package ru.ifmo.md.exam1;

import android.content.AsyncTaskLoader;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import java.util.ArrayList;

public class MyAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Cursor>> {
    Context context;

    public MyAsyncTaskLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public ArrayList<Cursor> loadInBackground() {
        ArrayList<Cursor> arrayList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MyContentProvider.URI, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isBeforeFirst() && !cursor.isAfterLast()) {
                arrayList.add(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return arrayList;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
}