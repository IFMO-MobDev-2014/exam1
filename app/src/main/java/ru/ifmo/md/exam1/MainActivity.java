package ru.ifmo.md.exam1;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    SimpleCursorAdapter cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView taskList = (ListView)findViewById(R.id.cources_list);

        String[] Columns = new String[]{MyContentProvider.COLUMN_TASK_TEXT, MyContentProvider.COLUMN_TASK_DATE};
        int[] elements = new int[]{R.id.task_text_textview, R.id.task_date_textview};

        cursor = new SimpleCursorAdapter(this, R.layout.element, null, Columns, elements, 0);

        ArrayAdapter<String> a = new ArrayAdapter<String>(this, R.layout.simple_list_elem, ViewTaskActivity.marksList);
        final ListView tm = (ListView)findViewById(R.id.filter);
        tm.setAdapter(a);

        tm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 4) {
                    Cursor c = getContentResolver().query(MyContentProvider.TABLE_TASKS_URI,  null, "_id LIKE '" + id +"'", null, null, null);
                    cursor.swapCursor(c);
                }
                else {
                    Cursor c = getContentResolver().query(MyContentProvider.TABLE_TASKS_URI,  null, null, null, null, null);
                    cursor.swapCursor(c);
                }
            }
        });

        taskList.setAdapter(cursor);

        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ViewTaskActivity.class);
                intent.putExtra("id", id);
                Log.d("DEBUG", position + " " + id);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(0, null, this);
        Button addButton = (Button)findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
                intent.putExtra("id", 0);
                startActivity(intent);
            }
        });


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] Columns = new String[]{MyContentProvider.COLUMN_ID, MyContentProvider.COLUMN_TASK_TEXT, MyContentProvider.COLUMN_TASK_DATE, MyContentProvider.COLUMN_TASK_MARK};
        return new CursorLoader(this, MyContentProvider.TABLE_TASKS_URI, Columns, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursor.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
