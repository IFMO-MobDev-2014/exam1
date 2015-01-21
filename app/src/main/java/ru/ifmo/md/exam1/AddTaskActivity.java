package ru.ifmo.md.exam1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ru.ifmo.md.exam1.db.TODOListContentProvider;
import ru.ifmo.md.exam1.db.model.Category;
import ru.ifmo.md.exam1.db.model.CategoryTable;
import ru.ifmo.md.exam1.db.model.CategoryToTaskTable;
import ru.ifmo.md.exam1.db.model.Task;
import ru.ifmo.md.exam1.db.model.TaskTable;

/**
 * Created by Kirill on 21.01.2015.
 */
public class AddTaskActivity extends Activity {

    String[] names ;
    ArrayList<Integer> selectedItems ;
    boolean[] isSelected ;


    private HashMap<Category, Boolean> categories = new HashMap<>();
    private long curId = -1;
    private Task task = null;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_add);

        if (bundle != null)
            curId = bundle.getLong("id", -1);
        else
            curId = -1;
        Set<Long> have = new HashSet<>();
        Cursor cursor;
        if (curId != -1) {
            cursor = getContentResolver()
                    .query(TODOListContentProvider.CONTENT_URI_TASK, null, TaskTable._ID + " = ?", new String[]{""+curId}, null);
            cursor.moveToFirst();
            task = new Task();
            task.setTitle(cursor.getString(cursor.getColumnIndex(TaskTable.TITLE_COLUMN)));
            task.setText(cursor.getString(cursor.getColumnIndex(TaskTable.TEXT_COLUMN)));
            cursor.close();

            cursor = getContentResolver()
                    .query(TODOListContentProvider.CONTENT_URI_CATEGORY_TO_TASK, null, CategoryToTaskTable.TASK_ID_COLUMN + " = ?",
                            new String[]{""+curId}, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                have.add(cursor.getLong(cursor.getColumnIndex(CategoryToTaskTable.CATEGORY_ID_COLUMN)));
                cursor.moveToNext();
            }
            cursor.close();
        }


        cursor = getContentResolver().query(TODOListContentProvider.CONTENT_URI_CATEGORY, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(cursor.getColumnIndex(CategoryTable._ID));
            String name = cursor.getString(cursor.getColumnIndex(CategoryTable.NAME_COLUMN));
            Category category = new Category();
            category.setId(id);
            category.setName(name);
            if (have.contains(id)) {
                categories.put(category, true);
            } else {
                categories.put(category, false);
            }
            cursor.moveToNext();
        }
        cursor.close();

        if (task != null) {
            TextView titleView = (TextView)findViewById(R.id.title_edit_text);
            titleView.setText(task.getTitle());
            TextView textView = (TextView)findViewById(R.id.body_edit_text);
            textView.setText(task.getText());
        }
    }

    private Dialog createDialog() {
        names = new String[categories.size()];
        selectedItems = new ArrayList<>();
        isSelected = new boolean[categories.size()];
        int count = 0;
        for (Map.Entry<Category, Boolean> i : categories.entrySet()) {
            if (i.getValue()) {
                isSelected[count] = true;
                selectedItems.add(count);
            } else{
                isSelected[count] = false;
            }
            names[count] = i.getKey().getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Categories")
                .setMultiChoiceItems(names, isSelected,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    selectedItems.add(which);
                                } else if (selectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    selectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                        // Set the action buttons
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int count = 0;
                        for (Map.Entry<Category, Boolean> i : categories.entrySet()) {
                            if (selectedItems.contains(count)) {
                                i.setValue(true);
                            }
                            count++;
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                break;
            default:
                break;
        }

        return true;
    }

    private void save() {
        if (task == null)
            task = new Task();
        TextView titleView = (TextView)findViewById(R.id.title_edit_text);
        String title = titleView.getText().toString();
        TextView textView = (TextView)findViewById(R.id.body_edit_text);
        String text = textView.getText().toString();
        long published = System.currentTimeMillis() / 1000L ;

        task.setText(text);
        task.setTitle(title);
        task.setCreated(published);

        ContentValues values = new ContentValues();
        values.put(TaskTable.TEXT_COLUMN, task.getText());
        values.put(TaskTable.TITLE_COLUMN, task.getTitle());
        values.put(TaskTable.CREATED_COLUMN, task.getCreated());
        if (curId == -1) {
            Uri res = getContentResolver().insert(TODOListContentProvider.CONTENT_URI_TASK, values);
            curId = Integer.parseInt(res.getLastPathSegment());
        } else {
            Uri uri = Uri.withAppendedPath(TODOListContentProvider.CONTENT_URI_TASK, curId+"");
            getContentResolver().update(uri, values, null, null);
        }
        getContentResolver().delete(TODOListContentProvider.CONTENT_URI_CATEGORY_TO_TASK,
                CategoryToTaskTable.TASK_ID_COLUMN + " = ?", new String[] {curId+""});
        for (Map.Entry<Category, Boolean> i : categories.entrySet()) {
            if (i.getValue()) {
                ContentValues val = new ContentValues();
                val.put(CategoryToTaskTable.CATEGORY_ID_COLUMN, i.getKey().getId());
                val.put(CategoryToTaskTable.TASK_ID_COLUMN, curId);
                getContentResolver().insert(TODOListContentProvider.CONTENT_URI_CATEGORY_TO_TASK, val);
            }
        }

    }

    public void addTags(View view) {
        try {
            Dialog d = createDialog();
            d.show();
        }
        catch (Exception e) {
            e.printStackTrace();
            }
    }
}
