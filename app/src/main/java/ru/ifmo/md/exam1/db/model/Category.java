package ru.ifmo.md.exam1.db.model;

import java.util.ArrayList;

/**
 * Created by Kirill on 21.01.2015.
 */
public class Category {
    private long id;
    private String name;
    private int count;
    private ArrayList<Long> tasksId;

    public Category() {
    }

    public Category(String name, int count, ArrayList<Long> tasksId) {
        this.name = name;
        this.count = count;
        this.tasksId = tasksId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<Long> getTasksId() {
        return tasksId;
    }

    public void setTasksId(ArrayList<Long> tasksId) {
        this.tasksId = tasksId;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", count=" + count +
                ", tasksId=" + tasksId +
                '}';
    }
}