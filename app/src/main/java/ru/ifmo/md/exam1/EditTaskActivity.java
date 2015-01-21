package ru.ifmo.md.exam1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;


public class EditTaskActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        final long id = getIntent().getLongExtra("id", 1);

        ArrayAdapter<String> a = new ArrayAdapter<String>(this, R.layout.simple_list_elem, ViewTaskActivity.marksList);
        final ListView tm = (ListView)findViewById(R.id.marks_list);
        tm.setAdapter(a);

        final EditText tt = (EditText)findViewById(R.id.task_text_edit);

        if (id != 0) {
            Cursor c = getContentResolver().query(MyContentProvider.TABLE_TASKS_URI, null, "_id = '" + id +"'", null, null, null);
            c.moveToFirst();


            tt.setText(c.getString(1));
            String marks = c.getString(3);


            String[] tmm = marks.split(" ");
            if (!marks.isEmpty()) {
                for (int i = 0; i < tmm.length; i++) {
                    int j = Integer.parseInt(tmm[i]);
                    tm.setItemChecked(j, true);
                }
            }
        }

        Button b = (Button)findViewById(R.id.button_save);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put(MyContentProvider.COLUMN_TASK_TEXT, tt.getText().toString());
                SparseBooleanArray sb = tm.getCheckedItemPositions();
                String marks = "";
                for (int i = 0; i < sb.size(); i++) {
                    int key = sb.keyAt(i);
                    if (sb.get(key)) {
                        marks += i + " ";
                    }
                }
                marks = marks.trim();
                cv.put(MyContentProvider.COLUMN_TASK_MARK, marks);
                if (id == 0) {
                    String currentDateTimeString = DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date()).toString();
                    cv.put(MyContentProvider.COLUMN_TASK_DATE, currentDateTimeString);
                    getContentResolver().insert(MyContentProvider.TABLE_TASKS_URI, cv);
                }
                else {
                    getContentResolver().update(MyContentProvider.TABLE_TASKS_URI, cv, "_id = '" + id+ "' ", null);
                }
                Intent intent = new Intent(EditTaskActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_task, menu);
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
}
