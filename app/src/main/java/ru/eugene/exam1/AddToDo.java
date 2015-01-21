package ru.eugene.exam1;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.eugene.exam1.db.ToDoItem;
import ru.eugene.exam1.db.ToDoProvider;
import ru.eugene.exam1.db.ToDoSource;


public class AddToDo extends ActionBarActivity {
    private static final int PICK_REQUEST = 1;
    private String action;
    private String labels;
    private EditText newName;
    private EditText newDescription;
    private ToDoItem toDoItem;
    private Button editLabels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent date = getIntent();
        action = date.getStringExtra("action");
        setContentView(R.layout.activity_add_to_do);

        newName = (EditText) findViewById(R.id.newName);
        newDescription = (EditText) findViewById(R.id.newDescription);
        editLabels = (Button) findViewById(R.id.editLabels);

        toDoItem = new ToDoItem();

        if (action.equals("detail")) {
            editLabels.setText("view labels");
            newName.setText(getIntent().getStringExtra(ToDoSource.COLUMN_NAME));
            newDescription.setText(getIntent().getStringExtra(ToDoSource.COLUMN_DESCRIPTION));
            labels = getIntent().getStringExtra(ToDoSource.COLUMN_LABELS);
            newName.setEnabled(false);
            newDescription.setEnabled(false);
        }

    }

    public void finish(View v) {
        if (action.equals("add")) {
            String name = newName.getText().toString();
            String description = newDescription.getText().toString();

            if (name.isEmpty()) {
                Toast.makeText(this, "name is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            toDoItem.setName(name);
            toDoItem.setDescription(description);
            toDoItem.setData(System.currentTimeMillis());

            getContentResolver().insert(ToDoProvider.CONTENT_URI_TODO, toDoItem.generateContentValues());
        }
        close(v);
    }

    public void close(View v) {
        finish();
    }

    public void editLabels(View v) {
        Intent intent = new Intent(this, EditLabels.class);
        intent.putExtra("action", action);
        intent.putExtra("labels", labels);
        startActivityForResult(intent, PICK_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_REQUEST) {
            if (resultCode == RESULT_OK) {
                toDoItem.setLabels(data.getStringExtra("result"));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_to_do, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
