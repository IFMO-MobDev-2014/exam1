package ru.ifmo.mobdev.exam1;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import ru.ifmo.mobdev.exam1.db.TasksDatabase;

/**
 * @author sugakandrey
 */
public class TasksListAdapter extends CursorAdapter {
    public TasksListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.task_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(TasksDatabase.NAME));
        String date = cursor.getString(cursor.getColumnIndex(TasksDatabase.DATE));
        ((TextView) view.findViewById(R.id.name)).setText(name);
        ((TextView) view.findViewById(R.id.date)).setText(date);
    }
}
