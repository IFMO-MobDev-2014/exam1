package ru.eugene.exam1.db;

import android.content.ContentValues;

/**
 * Created by eugene on 1/21/15.
 */
public class ToDoItem {
    public int id;
    public String name;
    public String labels;
    public long data;
    public String description;

    public ToDoItem() {}

    public ToDoItem(String name, String labels, long data, String description) {
        this.name = name;
        this.labels = labels;
        this.data = data;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContentValues generateContentValues() {
        ContentValues values = new ContentValues();

        values.put(ToDoSource.COLUMN_NAME, name);
        values.put(ToDoSource.COLUMN_LABELS, labels);
        values.put(ToDoSource.COLUMN_DATA, data);
        values.put(ToDoSource.COLUMN_DESCRIPTION, description);

        return values;
    }
}
