package ru.ifmo.md.exam1;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends ActionBarActivity {


    private ListView lvTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cursor cursor = getContentResolver().query(TO_DO_URI, null, null,
                null, null);
        startManagingCursor(cursor);

        String from[] = { ToDoContentProvider.MAIN_TABLE_NAME, ToDoContentProvider.MAIN_TABLE_ADD_TIME };
        int to[] = { android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                /*R.layout.list_view_item*/ android.R.layout.simple_expandable_list_item_2, cursor, from, to);

        lvTask = (ListView) findViewById(R.id.listView);
        lvTask.setAdapter(adapter);


        lvTask.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
                                + id);
                        Intent intent = new Intent(view.getContext(), AddScren.class);
                        intent.putExtra("ID",id);
                        startActivity(intent);
                    }
                }
        );
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
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), AddScren.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    final String LOG_TAG = "myLogs";

    final Uri TO_DO_URI = Uri
            .parse("content://ru.ifmo.md.exam1/tasks");

}
