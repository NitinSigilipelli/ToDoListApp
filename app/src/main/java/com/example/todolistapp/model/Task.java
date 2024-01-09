package com.example.todolistapp.model;

public class Task {
    private String title;
    private String description;
    private String dueDate;
    private String priority;
    private String category;
    private String status;
    private boolean isNew;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
// Constructor, getters, and setters

    public Task(int id,String title, String description, String dueDate, String priority, String category, String status) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.id = id;
        this.priority = priority;
        this.category = category;
        this.status = status;
        isNew = true;
    }

    public Task() {
    }

    public Task(String title, String description, String dueDate, String priority, String category) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.category = category;
        this.status = "New";
    }
    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
