package ru.ifmo.md.exam1;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;


/**
 */
public class AddNewTaskDialog extends Dialog {
    EditText name, url;
    Spinner categories;
    TasksAdapter listAdapter;
    ListView lv;
    String labels;
    ContentResolver content;
    Context context;
    boolean flag = false;

    public AddNewTaskDialog(Context context) {
        super(context);
        this.context = context;
        setContentView(R.layout.new_task);
        labels = "";
        setTitle("Add new task dialog");
        name = (EditText) findViewById(R.id.rss_name);
        url = (EditText) findViewById(R.id.rss_url);
        categories = (Spinner) findViewById(R.id.spinner);
    }

    public void initFields(final ContentResolver content, final TasksAdapter listAdapter, final ListView lv) {
        this.listAdapter = listAdapter;
        this.content = content;
        this.lv = lv;

        Cursor c = content.query(MyContentProvider.TABLE_2, new String[]{MyContentProvider.NAME}, null, null, null);
        ArrayList<String> string = new ArrayList<String>();
        c.moveToFirst();
        while(!c.isAfterLast()) {
            String name = c.getString(c.getColumnIndex(MyContentProvider.NAME));
            string.add(name);
            c.moveToNext();
        }
        ArrayAdapter<String> adapter  =  new  ArrayAdapter<String>(
                context, android.R.layout.simple_list_item_1, string);
        categories.setAdapter(adapter);
        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(flag) {
                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    labels = labels.concat(", ").concat(textView.getText().toString());
                }else
                    flag = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        addKeyListeners();
        onAddButton();
    }

    private void addKeyListeners() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        name.requestFocus();

        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if(i == EditorInfo.IME_ACTION_SEND) {
                    url.requestFocus();
                    handled = true;
                }
                return handled;
            }
        });

        url.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if(i == EditorInfo.IME_ACTION_SEND) {
                    updateDatabaseAndListView();
                    dismiss();
                    handled = true;
                }
                return handled;
            }
        });
    }

    private void onAddButton() {
        Button addButton = (Button) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDatabaseAndListView();
                dismiss();
            }
        });
    }

    private void updateDatabaseAndListView() {
        String title = name.getText().toString();
        String desctiption = url.getText().toString();
        Date date = new Date();

        ContentValues values = new ContentValues();
        values.put(MyContentProvider.TITLE, title);
        values.put(MyContentProvider.DESCRIPTION, desctiption);
        values.put(MyContentProvider.DATE, date.toString());
        values.put(MyContentProvider.LABELS, labels);

        content.insert(MyContentProvider.TABLE_1, values);

        listAdapter.notifyDataSetChanged();
        lv.setAdapter(listAdapter);
    }
}