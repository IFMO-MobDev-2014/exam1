package ru.eugene.exam1.db;

import android.content.ContentValues;

/**
 * Created by eugene on 1/21/15.
 */
public class LabelItem {
    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContentValues generateContentValues() {
        ContentValues values = new ContentValues();

        values.put(LabelSource.COLUMN_NAME, name);

        return values;
    }
}
