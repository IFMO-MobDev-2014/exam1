package ru.ifmo.md.exam1;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.ifmo.md.exam1.db.model.TaskTable;

/**
 * Created by Kirill on 21.01.2015.
 */
public class TODOListAdapter extends CursorAdapter {
    private LayoutInflater inflater;

    private boolean noData = false;

    public TODOListAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.main_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String title = cursor.getString(cursor.getColumnIndex(TaskTable.TITLE_COLUMN));
        long created = cursor.getLong(cursor.getColumnIndex(TaskTable.CREATED_COLUMN));
        Date date = new Date(created * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
        String dateString = sdf.format(date);

        TextView titleView = (TextView) view.findViewById(R.id.TODO_title);
        titleView.setText(title);
        TextView dateView = (TextView) view.findViewById(R.id.TODO_date);
        dateView.setText(dateString);
    }
}
