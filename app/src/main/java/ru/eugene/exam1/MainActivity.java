package ru.eugene.exam1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import ru.eugene.exam1.db.LabelItem;
import ru.eugene.exam1.db.ToDoItem;
import ru.eugene.exam1.db.ToDoProvider;
import ru.eugene.exam1.db.ToDoSource;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter adapter;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        LabelItem labelItem = new LabelItem();
//        labelItem.setName("matan");
//        getContentResolver().insert(ToDoProvider.CONTENT_URI_LABEL, labelItem.generateContentValues());
//
//        labelItem = new LabelItem();
//        labelItem.setName("diff");
//        getContentResolver().insert(ToDoProvider.CONTENT_URI_LABEL, labelItem.generateContentValues());
//
//        ToDoItem item = new ToDoItem();
//        item.setData(System.currentTimeMillis());
//        item.setDescription("description teorema");
//        item.setLabels("0");
//        item.setName("teorema");
//
//        getContentResolver().insert(ToDoProvider.CONTENT_URI_TODO, item.generateContentValues());

        String[] from = new String[] {ToDoSource.COLUMN_NAME, ToDoSource.COLUMN_DATA};
        int[] to = new int[] {R.id.name, R.id.data};

        adapter = new SimpleCursorAdapter(this, R.layout.item_todo, null, from, to, 0) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =  super.getView(position, convertView, parent);
                TextView date = (TextView) view.findViewById(R.id.data);
                long milliS = Long.parseLong(date.getText().toString());

                Date dateMy = new Date(milliS);
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                format.setTimeZone(cal.getTimeZone());

                date.setText(format.format(dateMy));

                return view;
            }
        };

        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

        getLoaderManager().initLoader(1, null, this);
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
        if (id == R.id.action_new) {
            Intent addToDo = new Intent(this, AddToDo.class);
            addToDo.putExtra("action", "add");
            startActivity(addToDo);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ToDoProvider.CONTENT_URI_TODO, null, null, null, ToDoSource.COLUMN_DATA);
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
