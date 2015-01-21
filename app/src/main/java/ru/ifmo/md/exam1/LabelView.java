package ru.ifmo.md.exam1;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import ru.ifmo.md.exam1.database.LabelDatabaseHelper;

/**
 * Created by heat_wave on 21.01.15.
 */
public class LabelView extends ListActivity {

    private LabelDatabaseHelper db;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.label_list);
        this.getListView().setDividerHeight(2);
        db = new LabelDatabaseHelper(this);
        adapter = new ArrayAdapter<String>(this, R.layout.label_list);
        Cursor resultSet = db.getReadableDatabase().rawQuery("Select * from labels", null);
        resultSet.moveToFirst();
        do {
            adapter.add(resultSet.getString(0));
        } while (resultSet.moveToNext());
        setListAdapter(adapter);
        registerForContextMenu(getListView());

    }

    // Create the menu based on the XML defintion
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    // Reaction to the menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert:
                EditText text = (EditText) findViewById(R.id.label_new);
                db.getWritableDatabase().execSQL("INSERT INTO labels VALUES(" + text.getText().toString() +");");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
