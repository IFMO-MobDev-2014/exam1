package ru.ifmo.md.exam1;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Svet on 21.01.2015.
 */
public class LabelsActivity extends Activity {


    ListView listView;
    EditText text;
    ArrayAdapter<String> adapter;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.labels_activity);

        text = (EditText) findViewById(R.id.edit_text);

        Cursor c = getContentResolver().query(MyContentProvider.TABLE_2, new String[]{MyContentProvider.NAME}, null,null, null);
        c.moveToFirst();
        ArrayList<String> strings = new ArrayList<String>();
        while(!c.isAfterLast()) {
            strings.add(c.getString(c.getColumnIndex(MyContentProvider.NAME)));
            c.moveToNext();
        }

        listView = (ListView) findViewById(R.id.labels_list);
        adapter  =  new  ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, strings);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView label = (TextView) view.findViewById(android.R.id.text1);
                getContentResolver().delete(MyContentProvider.TABLE_2, MyContentProvider.NAME + " = ?", new String[]{label.getText().toString()});
                adapter.remove(label.getText().toString());
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        listView.setAdapter(adapter);
    }

    public void onNewLabel(View view) {
        ContentValues value = new ContentValues();
        value.put(MyContentProvider.NAME, text.getText().toString());
        getContentResolver().insert(MyContentProvider.TABLE_2, value);
        adapter.notifyDataSetChanged();
    }
}
