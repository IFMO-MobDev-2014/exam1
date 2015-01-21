package ru.ifmo.md.exam1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by delf on 21.01.15.
 */
public class AddScren extends Activity {

    final String LOG_TAG = "myLogs";

    final Uri TO_DO_URI = Uri
            .parse("content://ru.ifmo.md.exam1/tasks");


    long RawId;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_scren);

        Intent intent = getIntent();
        RawId = intent.getLongExtra("ID", -1);

        String name = "";
        String full_text = "";
        int home = 0;
        int work = 0;
        int bug = 0;



        if(RawId != -1)
        {
            Cursor result = getContentResolver().query(TO_DO_URI,null,ToDoContentProvider.MAIN_TABLE_ID + " = ? ",new String[]{Long.toString(RawId)},"");
            result.moveToFirst();
            name = result.getString(result.getColumnIndex(ToDoContentProvider.MAIN_TABLE_NAME));
            full_text = result.getString(result.getColumnIndex(ToDoContentProvider.MAIN_TABLE_FULL_TEXT));
            home = result.getInt(result.getColumnIndex(ToDoContentProvider.MAIN_TABLE_HOME));
            work = result.getInt(result.getColumnIndex(ToDoContentProvider.MAIN_TABLE_WORK));
            bug = result.getInt(result.getColumnIndex(ToDoContentProvider.MAIN_TABLE_BUG));
        }

        Button button = (Button) findViewById(R.id.deleteButton);
        if(RawId == -1)
        {
            button.setText("Cancel");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
            });
        }
        else
        {
            ((TextView) findViewById(R.id.editTextName)).setText(name);
            ((TextView) findViewById(R.id.editTextFullText)).setText(full_text);
            ((CheckBox) findViewById(R.id.home)).setChecked(home == 1);
            ((CheckBox) findViewById(R.id.work)).setChecked(work== 1);
            ((CheckBox) findViewById(R.id.bug)).setChecked(bug== 1);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getContentResolver().delete(TO_DO_URI, ToDoContentProvider.MAIN_TABLE_ID +" = ? ",new String[]{Long.toString(RawId)});
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
            });
        }


        (findViewById(R.id.save_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText) findViewById(R.id.editTextName)).getText().toString().trim();
                String fullText = ((EditText) findViewById(R.id.editTextFullText)).getText().toString().trim();
                if ("".equals(name)) {
                    Toast.makeText(view.getContext(), "name invalid", Toast.LENGTH_LONG).show();
                    return;
                }
                ContentValues content = new ContentValues();
                Intent intent = new Intent();
                intent.putExtra(ToDoContentProvider.MAIN_TABLE_NAME, name);
                intent.putExtra(ToDoContentProvider.MAIN_TABLE_FULL_TEXT, fullText);
                content.put(ToDoContentProvider.MAIN_TABLE_NAME, name);
                content.put(ToDoContentProvider.MAIN_TABLE_FULL_TEXT, fullText);
                content.put(ToDoContentProvider.MAIN_TABLE_HOME, ((CheckBox) findViewById(R.id.home)).isChecked() ? 1 : 0 );
                content.put(ToDoContentProvider.MAIN_TABLE_WORK, ((CheckBox) findViewById(R.id.work)).isChecked() ? 1 : 0 );
                content.put(ToDoContentProvider.MAIN_TABLE_BUG, ((CheckBox) findViewById(R.id.bug)).isChecked() ? 1 : 0 );
                if(RawId !=-1)
                {
                    getContentResolver().update(TO_DO_URI, content,ToDoContentProvider.MAIN_TABLE_ID + " = ? ",new String[]{Long.toString(RawId)});
                }else
                {
                    getContentResolver().insert(TO_DO_URI,content);
                }
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }
}
