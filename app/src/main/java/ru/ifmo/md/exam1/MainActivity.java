package ru.ifmo.md.exam1;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;


public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    String sortOrder = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
        setListAdapter(new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null, new String[]{"name", "date"}, new int[]{android.R.id.text1, android.R.id.text2 }, 0));
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                MyDialog addDialog = new MyDialog();
                Bundle args = new Bundle();
                args.putBoolean(MyDialog.ARG_EDIT, true);
                args.putLong(MyDialog.ARG_ID, id);
                Cursor item = (Cursor) adapterView.getItemAtPosition(i);
                args.putString(MyDialog.ARG_NAME, item.getString(item.getColumnIndex("name")));
                args.putString(MyDialog.ARG_DATE, item.getString(item.getColumnIndex("date")));
                args.putString(MyDialog.ARG_DESC, item.getString(item.getColumnIndex("description")));
                args.putString(MyDialog.ARG_CATEGORY, item.getString(item.getColumnIndex("category")));
                addDialog.setArguments(args);
                getFragmentManager().beginTransaction().add(addDialog, "").commit();
            }

        });
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                getContentResolver().delete(MyContentProvider.URI, "" + id, null);
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            MyDialog myDialog = new MyDialog();
            Bundle args = new Bundle();
            args.putBoolean(MyDialog.ARG_EDIT, false);
            myDialog.setArguments(args);
            getFragmentManager().beginTransaction().add(myDialog, "").commit();
            return true;
        }

        if (id == R.id.action_sort_by_name) {
            sortOrder = "name" + " ASC";

            getLoaderManager().restartLoader(0, null, MainActivity.this);
        }

        if (id == R.id.action_sort_by_date) {
            sortOrder = "date" + " ASC";
            getLoaderManager().restartLoader(0, null, MainActivity.this);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MyContentProvider.URI, null, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            ((CursorAdapter) getListAdapter()).swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((CursorAdapter) getListAdapter()).swapCursor(null);
    }
}
