package com.example.todolistapp.localstorage;

import android.provider.BaseColumns;

public final class TaskContract {
    private TaskContract() {}

    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DUE_DATE = "due_date";
        public static final String COLUMN_PRIORITY = "priority";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_STATUS = "status";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TITLE + " TEXT NOT NULL, " +
                        COLUMN_DESCRIPTION + " TEXT, " +
                        COLUMN_DUE_DATE + " TEXT NOT NULL, " +
                        COLUMN_PRIORITY + " TEXT NOT NULL, " +
                        COLUMN_CATEGORY + " TEXT NOT NULL, " +
                        COLUMN_STATUS + " TEXT);";

        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
