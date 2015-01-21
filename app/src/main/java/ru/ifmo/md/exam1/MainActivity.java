package ru.ifmo.md.exam1;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import ru.ifmo.md.exam1.db.TODOListContentProvider;
import ru.ifmo.md.exam1.db.model.CategoryTable;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView rootView;
    private TODOListAdapter todoListAdapter;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**/
        Cursor c = getContentResolver().query(TODOListContentProvider.CONTENT_URI_CATEGORY, null, null, null, null);
        c.moveToFirst();
        if (c.getCount() == 0) {
            ContentValues val = new ContentValues();
            val.put(CategoryTable.NAME_COLUMN, "Study");
            getContentResolver().insert(TODOListContentProvider.CONTENT_URI_CATEGORY, val);
            val = new ContentValues();
            val.put(CategoryTable.NAME_COLUMN, "Work");
            getContentResolver().insert(TODOListContentProvider.CONTENT_URI_CATEGORY, val);
            val = new ContentValues();
            val.put(CategoryTable.NAME_COLUMN, "Home");
            getContentResolver().insert(TODOListContentProvider.CONTENT_URI_CATEGORY, val);
        }
        c.close();

        activity = this;
        setContentView(R.layout.activity_main);
        rootView = (ListView)findViewById(R.id.list);
        todoListAdapter = new TODOListAdapter(this
                , null, 0);
        rootView.setAdapter(todoListAdapter);
        rootView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, AddTaskActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        todoListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
        todoListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, AddTaskActivity.class);
                intent.putExtra("id", -1);
                startActivity(intent);
                break;
            case R.id.action_sort_by_date:
                break;
            case R.id.action_sort_by_title:
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(this,
                TODOListContentProvider.CONTENT_URI_TASK, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        todoListAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        todoListAdapter.swapCursor(null);
    }

}
