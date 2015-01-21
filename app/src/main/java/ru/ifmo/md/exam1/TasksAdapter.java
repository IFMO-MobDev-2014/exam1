package ru.ifmo.md.exam1;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Svet on 21.01.2015.
 */
public class TasksAdapter extends BaseAdapter {

    MainActivity dispatcher;
    Context context;

    TasksAdapter(MainActivity dispatcher, Context context) {
        this.dispatcher = dispatcher;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dispatcher.getTasksCount();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_task, null);
        TextView title = (TextView) v.findViewById(R.id.task_title);
        TextView labels = (TextView) v.findViewById(R.id.task_labels);
        Cursor c = dispatcher.getContentResolver().query(MyContentProvider.TABLE_1, new String[]{MyContentProvider.TITLE, MyContentProvider.LABELS}, MyContentProvider._ID + " = " + (i + 1), null, null);
        c.moveToFirst();
        Log.i("MESSAGE", "Columt index = " + c.getString(0));
        title.setText(c.getString(c.getColumnIndex(MyContentProvider.TITLE)));
        labels.setText(c.getString(c.getColumnIndex(MyContentProvider.LABELS)));

        return v;
    }
}
