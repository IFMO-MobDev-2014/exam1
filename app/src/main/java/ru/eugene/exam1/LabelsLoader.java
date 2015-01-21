package ru.eugene.exam1;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import ru.eugene.exam1.db.LabelSource;
import ru.eugene.exam1.db.ToDoProvider;

/**
 * Created by eugene on 1/21/15.
 */
public class LabelsLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;
    private ArrayAdapter<String> adapter;
    public ArrayList<String> listName;
    public ArrayList<String> listId;

    public ArrayAdapter<String> getAdapter() {
        return adapter;
    }

    LabelsLoader(Context context) {
        this.context = context;
        listName = new ArrayList<>();
        listId = new ArrayList<>();
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listName);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(context, ToDoProvider.CONTENT_URI_LABEL, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        listName.clear();
        if (data.moveToFirst()) {
            do {
               listName.add(data.getString(data.getColumnIndex(LabelSource.COLUMN_NAME)));
               listId.add(data.getString(data.getColumnIndex(LabelSource.COLUMN_ID)));
            } while (data.moveToNext());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.clear();
    }
}
