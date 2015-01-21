package ru.ifmo.md.exam1;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ViewTaskActivity extends ActionBarActivity {

    public final static String[] marksList = {"home", "work", "study", "other", "with all marks"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        final long id = getIntent().getLongExtra("id", 1);
        Cursor c = getContentResolver().query(MyContentProvider.TABLE_TASKS_URI, null, "_id = '" + id +"'", null, null, null);
        c.moveToFirst();

        TextView tt = (TextView)findViewById(R.id.task_text2_textview);
        TextView td = (TextView)findViewById(R.id.task_date2_txtview);
        TextView tm = (TextView)findViewById(R.id.task_marks2_textview);

        tt.setText(c.getString(1));
        td.setText(c.getString(2));

        String indMarks = c.getString(3);

        c.close();
        String[] im = indMarks.split(" ");
        String marksFinal = "";
        for (int i = 0; i<im.length; i++) {
            if (im[i].equals("0")) {
                marksFinal += marksList[0] + " ";
            } else if (im[i].equals("1")) {
                marksFinal += marksList[1] + " ";
            } else if (im[i].equals("2")) {
                marksFinal += marksList[2] + " ";
            } else if (im[i].equals("3")) {
                marksFinal += marksList[3] + " ";
            }
        }
        tm.setText(marksFinal);

        Button editTaskButton = (Button)findViewById(R.id.edit_task_button);
        editTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewTaskActivity.this, EditTaskActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_task, menu);
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
