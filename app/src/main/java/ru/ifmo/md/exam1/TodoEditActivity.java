package ru.ifmo.md.exam1;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import ru.ifmo.md.exam1.database.LabelsTable;
import ru.ifmo.md.exam1.database.TodoProvider;
import ru.ifmo.md.exam1.database.TodoTable;

/**
 * Created by sergey on 21.01.15.
 */
public class TodoEditActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_TODO_ID = "extra_todo_id";
    public static final long FLAG_ADD_TODO = -1;

    private long todoId;

    private EditText mSummaryET;
    private EditText mDescriptionET;
    private Button confirmButton;
    private TextView addedLabels;
    private TextView times;
    private Cursor cursor;
    private ListView lvAvailableLables;

    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.todo_edit_activity);

        mSummaryET = (EditText) findViewById(R.id.todo_edit_summary);
        mDescriptionET = (EditText) findViewById(R.id.todo_edit_description);
        confirmButton = (Button) findViewById(R.id.todo_edit_button);
        addedLabels = (TextView) findViewById(R.id.et_added_labels);
        times = (TextView) findViewById(R.id.tv_time);

        lvAvailableLables = (ListView) findViewById(R.id.list_available_labels);
        adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                null,
                new String[] {LabelsTable.COLUMN_DESCRIPTION},
                new int[] {android.R.id.text1}, 0
        );
        lvAvailableLables.setAdapter(adapter);

        lvAvailableLables.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor1 = getContentResolver().query(
                        TodoProvider.buildLabelUri(String.valueOf(id)),
                        null, null, null, null);
                String newLabel = cursor1.getString(cursor1.getColumnIndex(LabelsTable.COLUMN_DESCRIPTION));
                cursor1.close();

                String already = addedLabels.getText().toString();
                already = already + "|" + newLabel;
                addedLabels.setText(already);
            }
        });



        getLoaderManager().initLoader(0, null, this);


        Bundle extras = getIntent().getExtras();
        todoId = (bundle == null) ? -1 : bundle.getLong(EXTRA_TODO_ID);
        if (extras != null) {
            todoId = extras.getLong(EXTRA_TODO_ID);
            updateUI();
        }

        if (todoId != -1) {
            cursor = getContentResolver().query(
                    TodoProvider.buildTodoUri(String.valueOf(todoId)),
                    null, null, null, null);
            updateUI();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void updateUI() {
        if (cursor != null) {
            cursor.moveToFirst();
            String summary = cursor.getString(cursor.getColumnIndex(TodoTable.COLUMN_SUMMARY));
            String desription = cursor.getString(cursor.getColumnIndex(TodoTable.COLUMN_DESCRIPTION));
            String category = cursor.getString(cursor.getColumnIndex(TodoTable.COLUMN_CATEGORY));
            String pubDate = cursor.getString(cursor.getColumnIndex(TodoTable.COLUMN_PUB_DATE));
            mSummaryET.setText(summary);
            mDescriptionET.setText(desription);
            addedLabels.setText(category);
            times.setText(pubDate);
//            String[] parts = category.split("\\|");
        }
    }


    public void confirmClicked(View view) {
        if (TextUtils.isEmpty(mSummaryET.getText().toString())) {
            Toast.makeText(TodoEditActivity.this, getResources().getString(R.string.non_empty_summary), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
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
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_labels) {
            Intent intent = new Intent(this, LabelsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}