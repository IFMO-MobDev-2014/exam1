package ru.ya.exam.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Calendar;

import ru.ya.exam.MyType;
import ru.ya.exam.R;
import ru.ya.exam.database.MContentProvider;
import ru.ya.exam.database.MSQLiteHelper;


public class EditActivity extends ActionBarActivity {
    Integer itemId;
    EditText titleView;
    EditText descriptionView;
    ListView listView;
    ArrayAdapter<MyType> adapter;
    MyType [] array;
    int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        listView = (ListView) findViewById(R.id.list_edit_activity);

        Intent intent = getIntent();
        itemId = intent.getIntExtra(MainActivity.ITEM_ID, -1);
        if (itemId == -1) throw new Error();

        titleView = (EditText) findViewById(R.id.edit_title);
        descriptionView = (EditText) findViewById(R.id.edit_description);

        Cursor cursor = getContentResolver().query(MContentProvider.ITEM_URI, null, MSQLiteHelper.COLUMN_ID + "=?", new String[]{itemId.toString()}, null);
        if (cursor.getCount() != 1) throw new Error();
        cursor.moveToFirst();
        titleView.setText(cursor.getString(cursor.getColumnIndex(MSQLiteHelper.COLUMN_TITLE)));
        descriptionView.setText(cursor.getString(cursor.getColumnIndex(MSQLiteHelper.COLUMN_DESCRIPTION)));
        cursor.close();

        cursor = getContentResolver().query(MContentProvider.ALL_LABEL_URI, null, null, null, null);
        n = cursor.getCount();
        array = new MyType[n];

        for (int i = 0; i < n; i++)
            array[i] = new MyType();

        for (int i = 0; i < n; i++) {
            cursor.moveToPosition(i);
            array[i].setLable(cursor.getString(cursor.getColumnIndex(MSQLiteHelper.COLUMN_LABEL)));
        }
        cursor.close();

        cursor = getContentResolver().query(MContentProvider.LABEL_URI, null, null, null, null);
        int m = cursor.getCount();
        for (int i = 0; i < m; i++) {
            cursor.moveToPosition(i);
            String label = cursor.getString(cursor.getColumnIndex(MSQLiteHelper.COLUMN_LABEL));
            for (int j = 0; j < n; j++)
                if (array[i].getLable().equals(label)) {
                    array[i].setFlag(true);
                }
        }

       adapter = new ArrayAdapter<MyType>(this, R.layout.label) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View mView;
                if (convertView == null) mView = LayoutInflater.from(EditActivity.this).inflate(R.layout.label, parent, false);
                else mView = convertView;

                CheckBox checkBox = (CheckBox)mView.findViewById(R.id.check_box);
                Log.e("check Box:", "" + (checkBox == null));
                MyType data = getItem(position);
                checkBox.setText(data.getLable());
                checkBox.setPressed(data.isFlag());
                return mView;
            }
        };
        for (int i = 0; i < n; i++) {
            Log.e("adapter null: ", (adapter == null) + "");
            Log.e("array[i] null: ", (array[i] == null) + "");
            adapter.add(array[i]);
        }


        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox)view.findViewById(R.id.check_box);
                Boolean value = checkBox.isPressed();
                if (value == false) value = true;
                else value = false;
                checkBox.setPressed(value);
                array[(int)id].setFlag(value);



                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_label) {
            Calendar tmp = Calendar.getInstance();
            String time = tmp.getTime().toString();
            String title = titleView.getText().toString();
            String description = descriptionView.getText().toString();

            ContentValues values = new ContentValues();
            values.put(MSQLiteHelper.COLUMN_TITLE, title);
            values.put(MSQLiteHelper.COLUMN_DESCRIPTION, description);
            values.put(MSQLiteHelper.COLUMN_DATE, time);
            getContentResolver().insert(MContentProvider.ITEM_URI, values);

            for (int i = 0; i < n; i++) {
                if (array[i].isSe)

            }

        }
        return super.onOptionsItemSelected(item);
    }
}
