package ru.ifmo.md.exam1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity {

    ListView listView;
    Context context;
    TasksAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContentValues values = new ContentValues();
        values.put(MyContentProvider.NAME, "Work");
        getContentResolver().insert(MyContentProvider.TABLE_2, values);

        setContentView(R.layout.activity_main);
        context = this;
        listView = (ListView) findViewById(R.id.list_tasks);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TextView title = (TextView) view.findViewById(R.id.task_title);
                Cursor c = getContentResolver().query(MyContentProvider.TABLE_1, new String[]{MyContentProvider.TITLE, MyContentProvider.LABELS, MyContentProvider.DATE, MyContentProvider.DESCRIPTION},
                        MyContentProvider.TITLE + " = ? ", new String[]{title.getText().toString()}, null);
                c.moveToFirst();

                Intent intent = new Intent(context, FullDescriptionActivity.class);
                intent.putExtra(MyContentProvider.DESCRIPTION, c.getString(c.getColumnIndex(MyContentProvider.DESCRIPTION)));
                intent.putExtra(MyContentProvider.LABELS, c.getString(c.getColumnIndex(MyContentProvider.LABELS)));
                intent.putExtra(MyContentProvider.DATE, c.getString(c.getColumnIndex(MyContentProvider.DATE)));
                intent.putExtra(MyContentProvider.TITLE, c.getString(c.getColumnIndex(MyContentProvider.TITLE)));
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                EditTaskDialog dialog = new EditTaskDialog(context);
                TextView title = (TextView) view.findViewById(R.id.task_title);
                Cursor c = getContentResolver().query(MyContentProvider.TABLE_1, new String[]{MyContentProvider.TITLE, MyContentProvider.DATE, MyContentProvider.DESCRIPTION}, MyContentProvider.TITLE + " = ?", new String[]{title.getText().toString()}, null);
                c.moveToFirst();
                String description = c.getString(c.getColumnIndex(MyContentProvider.DESCRIPTION));
                String date = c.getString(c.getColumnIndex(MyContentProvider.DATE));
                dialog.initFields(getContentResolver(), adapter, listView, title.getText().toString(), description, date);
                dialog.show();
                return true;
            }
        });
        adapter = new TasksAdapter(this, this);
        listView.setAdapter(adapter);
    }


    public void labelss(View v) {
        Intent intent = new Intent(this, LabelsActivity.class);
        startActivity(intent);
    }
    public int getLabelsCount() {
        Cursor c = getContentResolver().query(MyContentProvider.TABLE_2, null, null, null, null);
        Log.i("MESSAGE", "getLabelCount " + c.getCount());
        return c.getCount();
    }

    public int getTasksCount() {
        Cursor c = getContentResolver().query(MyContentProvider.TABLE_1, null, null, null, null);
        Log.i("MESSAGE", "getTasksCount " + c.getCount());
        return c.getCount();
    }

    public void onNewTaskClick(View v) {
        AddNewTaskDialog dialog = new AddNewTaskDialog(this);
        dialog.initFields(getContentResolver(), adapter, listView);
        dialog.show();
    }
}
