package ru.ifmo.md.exam1.db.model;

/**
 * Created by Kirill on 21.01.2015.
 */
public class CategoryToTask {
    private long categoryId;
    private long taskId;

    public CategoryToTask(long categoryId, long taskId) {
        this.categoryId = categoryId;
        this.taskId = taskId;
    }

    public CategoryToTask() {
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "CategoryToTask{" +
                "categoryId=" + categoryId +
                ", taskId=" + taskId +
                '}';
    }
}
