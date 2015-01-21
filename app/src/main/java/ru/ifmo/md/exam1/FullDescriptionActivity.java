package ru.ifmo.md.exam1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.AccessibleObject;

/**
 * Created by Svet on 21.01.2015.
 */
public class FullDescriptionActivity extends Activity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.task_description);

        Intent intent = getIntent();
        String title = intent.getStringExtra(MyContentProvider.TITLE);
        String labels = intent.getStringExtra(MyContentProvider.LABELS);
        String date = intent.getStringExtra(MyContentProvider.DATE);
        String description = intent.getStringExtra(MyContentProvider.DESCRIPTION);

        TextView viewTitle = (TextView) findViewById(R.id.description_title);
        TextView viewLabels = (TextView) findViewById(R.id.description_labesl);
        TextView viewDate = (TextView) findViewById(R.id.description_date);
        TextView viewDescription = (TextView) findViewById(R.id.description_description);
        viewTitle.setText(title);
        viewLabels.setText(labels);
        viewDate.setText(date);
        viewDescription.setText(description);

    }

    public void onEditClick(View v) {

    }
}
