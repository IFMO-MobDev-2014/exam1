package ru.ifmo.mobdev.exam1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import ru.ifmo.mobdev.exam1.db.TasksDatabase;

/**
 * @author sugakandrey
 */
public class SingleTaskActivity extends Activity {
    private String name;
    private String description;
    private String labels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_task);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        name = extras.getString(TasksDatabase.NAME);
        description = extras.getString(TasksDatabase.DESCRIPTION);
        labels = extras.getString(TasksDatabase.LABELS);
        ((TextView) findViewById(R.id.single_task_name)).setText(name);
        ((TextView) findViewById(R.id.single_task_description)).setText(description);
        ((TextView) findViewById(R.id.single_task_labels)).setText(labels);

    }
}
