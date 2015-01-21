package ru.ifmo.mobdev.exam1;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import ru.ifmo.mobdev.exam1.db.CategoriesDatabase;
import ru.ifmo.mobdev.exam1.db.MyContentProvider;
import ru.ifmo.mobdev.exam1.db.TasksDatabase;


public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>, AddTaskDialog.OnCompleteListener {
    public static final String LABLES = "lables";
    public static final String LABLES_LIST = "lables list";
    private TasksListAdapter adapter;
    private static String ARRAY_DIVIDER = " , ";
    private String currentLabel;
    public static final String EDIT = "edit";

    public static String serialize(String content[]) {
        return TextUtils.join(ARRAY_DIVIDER, content);
    }

    public static String[] derialize(String content) {
        return content.split(ARRAY_DIVIDER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView list = (ListView) findViewById(R.id.tasks_list);
        adapter = new TasksListAdapter(this, null, false);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = adapter.getCursor();
                String name = cursor.getString(cursor.getColumnIndex(TasksDatabase.NAME));
                String description = cursor.getString(cursor.getColumnIndex(TasksDatabase.DESCRIPTION));
                String labels = "Categories: " + cursor.getString(cursor.getColumnIndex(TasksDatabase.LABELS));
                Intent intent = new Intent(MainActivity.this, SingleTaskActivity.class);
                intent.putExtra(TasksDatabase.NAME, name);
                intent.putExtra(TasksDatabase.DESCRIPTION, description);
                intent.putExtra(TasksDatabase.LABELS, labels);
                startActivity(intent);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                getContentResolver().delete(MyContentProvider.CONTENT_URI_TASKS, TasksDatabase._ID + " = " + id, null);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AddTaskDialog addTaskDialog = new AddTaskDialog();
                Cursor cursor = adapter.getCursor();
                String description = cursor.getString(cursor.getColumnIndex(TasksDatabase.DESCRIPTION));
                String[] labels = derialize(cursor.getString(cursor.getColumnIndex(TasksDatabase.LABELS)));
                String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
                ArrayList<String> labelsList = new ArrayList<String>();
                Collections.addAll(labelsList, labels);
                Bundle bundle = new Bundle();
                bundle.putBoolean(EDIT, true);
                bundle.putStringArrayList(LABLES_LIST, labelsList);
                bundle.putString(TasksDatabase.NAME, name);
                bundle.putString(TasksDatabase.DESCRIPTION, description);
                bundle.putStringArray(LABLES, labels);
                addTaskDialog.setArguments(bundle);
                addTaskDialog.show(ft, "dialog");
                return true;
            }
        });
        list.setAdapter(adapter);
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MyContentProvider.CONTENT_URI_TASKS, new String[]{
                TasksDatabase._ID,
                TasksDatabase.NAME, TasksDatabase.LABELS, TasksDatabase.DESCRIPTION, TasksDatabase.DATE
        }, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);

    }

    public void onAddClick(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        AddTaskDialog addTaskDialog = new AddTaskDialog();
        Bundle bundle = new Bundle();
        ArrayList<String> lables = new ArrayList<String>();
        Cursor cursor = getContentResolver().query(MyContentProvider.CONTENT_URI_LABLES, new String[]{CategoriesDatabase.NAME}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            lables.add(cursor.getString(cursor.getColumnIndex(CategoriesDatabase.NAME)));
            cursor.moveToNext();
        }
        bundle.putStringArrayList(LABLES_LIST, lables);
        bundle.putBoolean(EDIT, false);
        addTaskDialog.setArguments(bundle);
        addTaskDialog.show(ft, "dialog");
    }

    @Override
    public void onComplete(String name, String description, String[] labels) {
        ContentValues cv = new ContentValues();
        cv.put(TasksDatabase.NAME, name);
        cv.put(TasksDatabase.DESCRIPTION, description);
        cv.put(TasksDatabase.DATE, new Date().toString());
        if (currentLabel == null){
            currentLabel = "-";
        }
        cv.put(TasksDatabase.LABELS, currentLabel);
        getContentResolver().insert(MyContentProvider.CONTENT_URI_TASKS, cv);
        currentLabel = null;
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onLabelSelected(String s) {
        if (currentLabel == null) {
            currentLabel = s;
        } else
            currentLabel += " , " + s;
    }

    @Override
    public void onLabelsRemoved(String s) {
        currentLabel = s;
    }
}
