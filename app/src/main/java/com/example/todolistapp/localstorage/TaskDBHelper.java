package com.example.todolistapp.localstorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    public TaskDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the "tasks" table
        db.execSQL(TaskContract.TaskEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade the database if needed
        db.execSQL(TaskContract.TaskEntry.DELETE_TABLE);
        onCreate(db);
    }
    public void deleteTask(long taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = TaskContract.TaskEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(taskId)};
        db.delete(TaskContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
    }
}
