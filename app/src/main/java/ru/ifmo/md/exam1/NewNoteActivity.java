package ru.ifmo.md.exam1;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class NewNoteActivity extends ActionBarActivity {

    int itemId = -1;
    EditText title, desc;
    CheckBox importantCheck, workCheck, tomorrowCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        title = (EditText) findViewById(R.id.editText);
        desc = (EditText) findViewById(R.id.editText2);
        workCheck = (CheckBox) findViewById(R.id.checkBox);
        importantCheck = (CheckBox) findViewById(R.id.checkBox2);
        tomorrowCheck = (CheckBox) findViewById(R.id.checkBox3);

        try {
            itemId = getIntent().getExtras().getInt("id");
            Cursor c = getContentResolver().query(TodoProvider.TODO_URI, null, TodoProvider._ID + "=?",
                                                     new String[]{Integer.toString(itemId)}, null);
            c.moveToNext();
            title.setText(c.getString(c.getColumnIndex(TodoProvider.TITLE)));
            desc.setText(c.getString(c.getColumnIndex(TodoProvider.DESC)));
            c.close();
            c = getContentResolver().query(TodoProvider.TAGS_URI, null, TodoProvider.NOTE_ID + "=?",
                                                     new String[]{Integer.toString(itemId)}, null);
            while (c.moveToNext()) {
                switch (c.getString(c.getColumnIndex(TodoProvider.NAME))) {
                    case "Important":
                        importantCheck.setChecked(true);
                        break;
                    case "Work":
                        workCheck.setChecked(true);
                        break;
                    case "I'll do it tomorrow":
                        tomorrowCheck.setChecked(true);
                        break;
                }
            }
        } catch (Exception ignored) {}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_note, menu);
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
            if (title.getText().toString().equals("")) {
                Toast t = Toast.makeText(this, "No title, no note", Toast.LENGTH_LONG);
                t.show();
            } else {
                if (itemId != -1) {
                    getContentResolver().delete(TodoProvider.TODO_URI, TodoProvider._ID + "=" + itemId,
                            null);
                    getContentResolver().delete(TodoProvider.TAGS_URI, TodoProvider.NOTE_ID + "=" + itemId,
                            null);
                }

                ContentValues cv = new ContentValues();
                cv.put(TodoProvider.TITLE, title.getText().toString());
                cv.put(TodoProvider.DESC, desc.getText().toString());
                cv.put(TodoProvider.DATE, MyCursorAdapter.dateFormat.
                        format(Calendar.getInstance().getTime()));
                getContentResolver().insert(TodoProvider.TODO_URI, cv);
                Cursor c = getContentResolver().query(TodoProvider.TODO_URI, null,
                                TodoProvider.TITLE + "=?", new String[]{title.getText().toString()},
                                null);
                c.moveToNext();
                // last added with this title
                long newID = c.getInt(c.getColumnIndex(TodoProvider._ID));
                if (importantCheck.isChecked()) {
                    cv = new ContentValues();
                    cv.put(TodoProvider.NAME, "Important");
                    cv.put(TodoProvider.NOTE_ID, newID);
                    getContentResolver().insert(TodoProvider.TAGS_URI, cv);
                }
                if (workCheck.isChecked()) {
                    cv = new ContentValues();
                    cv.put(TodoProvider.NAME, "Work");
                    cv.put(TodoProvider.NOTE_ID, newID);
                    getContentResolver().insert(TodoProvider.TAGS_URI, cv);
                }
                if (tomorrowCheck.isChecked()) {
                    cv = new ContentValues();
                    cv.put(TodoProvider.NAME, "I'll do it tomorrow");
                    cv.put(TodoProvider.NOTE_ID, newID);
                    getContentResolver().insert(TodoProvider.TAGS_URI, cv);
                }
            }
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
