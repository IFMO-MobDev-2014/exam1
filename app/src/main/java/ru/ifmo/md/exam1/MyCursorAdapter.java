package ru.ifmo.md.exam1;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MyCursorAdapter extends BaseAdapter{
    static class Entry {
        String title;
        Date date;
        public int id;
        ArrayList <String> marks;

        public Entry(String title, Date date, int id) {
            this.title = title;
            this.date = date;
            this.id = id;
            marks = new ArrayList<>();
        }
    }

    HashMap<Integer, Entry> data = new HashMap<>();
    ContentResolver resolver;
    static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
    Context mainContext;

    public MyCursorAdapter(Context mainContext) {
        resolver = mainContext.getContentResolver();
        this.mainContext = mainContext;

        refresh();
    }

    public int getId(int position) {
        return ((Entry)data.values().toArray()[position]).id;
    }

    public void refresh() {
        data.clear();

        Cursor c = resolver.query(TodoProvider.TODO_URI, null, null, null, null);
        if (c == null)
            return;

        while (c.moveToNext()) {
            try {
                data.put(c.getInt(c.getColumnIndex(TodoProvider._ID)),
                        new Entry(c.getString(c.getColumnIndex(TodoProvider.TITLE)),
                                dateFormat.parse(c.getString(c.getColumnIndex(TodoProvider.DATE))),
                                c.getInt(c.getColumnIndex(TodoProvider._ID))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        c = resolver.query(TodoProvider.TAGS_URI, null, null, null, null);
        String tag;
        while (c.moveToNext()) {
            if (!(tag = c.getString(c.getColumnIndex(TodoProvider.NAME))).equals("")) {
                Entry e = ((Entry)data.get(c.getInt(c.getColumnIndex(TodoProvider.NOTE_ID))));
                e.marks.add(tag);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.values().toArray()[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Entry curr = (Entry) data.values().toArray()[position];

        LayoutInflater inflater = (LayoutInflater)
                mainContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.linear_row, parent, false);
        TextView title = (TextView) rowView.findViewById(R.id.titleView);
        TextView tags = (TextView) rowView.findViewById(R.id.tagsView);
        TextView date = (TextView) rowView.findViewById(R.id.dateView);
        title.setText(curr.title);
        tags.setText("");
        for (String s: curr.marks) {
            tags.append(s + " ");
        }
        date.setText(dateFormat.format(curr.date));

        return rowView;
    }
}
