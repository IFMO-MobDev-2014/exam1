package ru.ifmo.md.exam1;

import java.util.ArrayList;

public class Item {
    private String date;
    private String name;
    private String description;
    private ArrayList<String> categories;

    public Item(String date, String name, String description, ArrayList<String> categories) {
        this.date = date;
        this.name = name;
        this.description = description;
        this.categories = categories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }
}
