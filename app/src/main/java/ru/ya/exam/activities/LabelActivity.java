package ru.ya.exam.activities;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import ru.ya.exam.R;
import ru.ya.exam.database.MContentProvider;
import ru.ya.exam.database.MSQLiteHelper;

public class LabelActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks <Cursor> {
    ListView listView;
    SimpleCursorAdapter adapter;
    TextView editView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);
        editView = (TextView)findViewById(R.id.edit_label_name);

        listView = (ListView)findViewById(R.id.list_edit_label);
        adapter = new SimpleCursorAdapter(this, R.layout.onelabel, null, new String[]{MSQLiteHelper.COLUMN_LABEL}, new int[]{R.id.label_name}, 0);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((TextView)view.findViewById(R.id.label_name)).getText().toString();
                getContentResolver().delete(MContentProvider.ALL_LABEL_URI, MSQLiteHelper.COLUMN_LABEL + "=?", new String[]{name});
                return false;
            }
        });
        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_label, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_new_label) {
            String name = editView.getText().toString();
            if (name == null || name.length() == 0) {
                Toast.makeText(LabelActivity.this, "empty label", Toast.LENGTH_LONG).show();
            }
            ContentValues values = new ContentValues();
            values.put(MSQLiteHelper.COLUMN_LABEL, name);
            getContentResolver().insert(MContentProvider.ALL_LABEL_URI, values);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MContentProvider.ALL_LABEL_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
