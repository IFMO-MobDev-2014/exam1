package ru.eugene.exam1;

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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

import ru.eugene.exam1.db.LabelSource;
import ru.eugene.exam1.db.ToDoProvider;


public class EditLabels extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private ListView list;
    private LabelsLoader labelsLoader;
    private SimpleCursorAdapter adapter;
    private ArrayList<String> listId;
    private ArrayList<Boolean> checked;
    private String result = "";
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_labels);

        action = getIntent().getStringExtra("action");

        list = (ListView) findViewById(R.id.listEditLabels);
        labelsLoader = new LabelsLoader(this);
        getLoaderManager().initLoader(2, null, labelsLoader);

        listId = new ArrayList<>();
        checked = new ArrayList<>();
        String[] from = new String[]{LabelSource.COLUMN_NAME};
        int[] to = new int[]{R.id.nameLabel};

        if (action.equals("detail")) {
            adapter = new SimpleCursorAdapter(this, R.layout.item_edit_labels2, null, from, to, 0);
        } else {
            adapter = new SimpleCursorAdapter(this, R.layout.item_edit_labels, null, from, to, 0);
        }

        list.setAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox box = ((CheckBox) view.findViewById(R.id.enable));
                box.setChecked(box.isChecked() ^ true);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_labels, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ToDoProvider.CONTENT_URI_LABEL, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        listId.clear();
        if (data.moveToFirst()) {
            do {
                listId.add(data.getString(data.getColumnIndex(LabelSource.COLUMN_ID)));
                checked.add(false);
            } while (data.moveToNext());
        }
        adapter.notifyDataSetChanged();
    }

    public void finish(View v) {
        int i = 0;
        for (Boolean b : checked) {
            if (b) {
                result = listId.get(i);
            } else {
                result += "|" + listId.get(i);
            }
            i++;
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", result);
        setResult(RESULT_OK, returnIntent);
        this.finish();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
