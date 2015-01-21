package ru.ifmo.md.exam1.db.model;

import java.util.ArrayList;

/**
 * Created by Kirill on 21.01.2015.
 */
public class Task {
    private String title;
    private String text;
    private long created;
    private ArrayList<Long> categoriesId;

    public Task() {
    }

    public Task(String title, String text, long created, ArrayList<Long> categoriesId) {
        this.title = title;
        this.text = text;
        this.created = created;
        this.categoriesId = categoriesId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public ArrayList<Long> getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(ArrayList<Long> categoriesId) {
        this.categoriesId = categoriesId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", created=" + created +
                ", categoriesId=" + categoriesId +
                '}';
    }
}
