package ru.ifmo.md.exam1;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
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

import java.util.Date;


/**
 */
public class EditTaskDialog extends Dialog {
    EditText name, url;
    TasksAdapter listAdapter;
    ListView lv;
    String labels;
    ContentResolver content;
    String data = "";
    boolean flag = false;

    public EditTaskDialog(Context context) {
        super(context);
        setContentView(R.layout.edit_task);
        labels = "";
        setTitle("Edit task dialog");
        name = (EditText) findViewById(R.id.rss_name_edit);

        url = (EditText) findViewById(R.id.rss_url_edit);
    }

    public void initFields(final ContentResolver content, final TasksAdapter listAdapter, final ListView lv, String title, String desctiption, String data) {
        this.listAdapter = listAdapter;
        this.content = content;
        this.data = data;
        this.lv = lv;

        name.setText(title);
        url.setText(desctiption);
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
        Button addButton = (Button) findViewById(R.id.edit_button);
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

        ContentValues values = new ContentValues();
        values.put(MyContentProvider.TITLE, title);
        values.put(MyContentProvider.DESCRIPTION, desctiption);

        int cnt = content.update(MyContentProvider.TABLE_1, values, MyContentProvider.DATE + " = ?", new String[]{data});
        Log.i("MESSAGE", "upd count = " + cnt);

        listAdapter.notifyDataSetChanged();
        lv.setAdapter(listAdapter);
    }
}