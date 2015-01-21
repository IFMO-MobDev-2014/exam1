package ru.ifmo.md.exam1;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import ru.ifmo.md.exam1.database.LabelsTable;
import ru.ifmo.md.exam1.database.TodoProvider;

/**
 * Created by sergey on 21.01.15.
 */
public class LabelsActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView listView;
    private SimpleCursorAdapter adapter;

    private String[] from = new String[] {LabelsTable.COLUMN_DESCRIPTION};
    private int[] to = new int[] {android.R.id.text1};

    private EditText newLabelEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.labels_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newLabelEditText = (EditText) findViewById(R.id.et_new_label);
        listView = (ListView) findViewById(R.id.lv_labels);
        adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_activated_1,
                null,
                from,
                to,
                0);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_context_todo, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_delete:
                Cursor cursor = (Cursor) adapter.getItem(acmi.position);
                String labelUri = String.valueOf(cursor.getInt(cursor.getColumnIndex(LabelsTable.COLUMN_ID)));
                getContentResolver().delete(TodoProvider.buildLabelUri(labelUri), null, null);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void addClicked(View view) {
        String labelName = newLabelEditText.getText().toString();
        if (TextUtils.isEmpty(labelName)) {
            Toast.makeText(LabelsActivity.this, getResources().getString(R.string.non_empty_descr), Toast.LENGTH_LONG).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(LabelsTable.COLUMN_DESCRIPTION, labelName);
        getContentResolver().insert(TodoProvider.CONTENT_URI_LABELS, values);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                TodoProvider.CONTENT_URI_LABELS,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
