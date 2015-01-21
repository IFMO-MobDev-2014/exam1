package ru.ifmo.md.exam1;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import ru.ifmo.md.exam1.database.TodoProvider;
import ru.ifmo.md.exam1.database.TodoTable;


public class MainActivity extends ActionBarActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener {

    private ListView listView;
    private SimpleCursorAdapter adapter;

    private static final String[] from = new String[] {TodoTable.COLUMN_SUMMARY, TodoTable.COLUMN_PUB_DATE};
    private static final int[] to = new int[] {android.R.id.text1, android.R.id.text2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new SimpleCursorAdapter(getApplicationContext(),
                android.R.layout.simple_expandable_list_item_2,
                null,
                from, to, 0);
        listView = (ListView) findViewById(R.id.lv_todos);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, TodoEditActivity.class);
        intent.putExtra(TodoEditActivity.EXTRA_TODO_ID, id);
        startActivity(intent);
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
                String todoUri = String.valueOf(cursor.getInt(cursor.getColumnIndex(TodoTable.COLUMN_ID)));
                getContentResolver().delete(TodoProvider.buildTodoUri(todoUri), null, null);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
        if (id == R.id.action_insert) {
            Intent intent = new Intent(this, TodoEditActivity.class);
            intent.putExtra(TodoEditActivity.EXTRA_TODO_ID, TodoEditActivity.FLAG_ADD_TODO);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                TodoProvider.CONTENT_URI_TODOS,
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
