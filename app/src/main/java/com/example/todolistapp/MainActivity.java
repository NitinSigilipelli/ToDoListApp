package com.example.todolistapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapp.adapter.TaskAdapter;
import com.example.todolistapp.localstorage.TaskContract;
import com.example.todolistapp.localstorage.TaskDBHelper;
import com.example.todolistapp.model.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AddTaskBottomSheetFragment.OnTaskAddedListener,TaskAdapter.OnItemClickListener {
    private TaskDBHelper dbHelper;

    private ArrayList<Task> taskList;
    private TextView dateTextView;
    private TextView monthTextView;
    private TextView yearTextView;
    private TaskAdapter adapter; // Create a custom adapter for your Task class
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dateTextView = findViewById(R.id.dateTextView);
        monthTextView = findViewById(R.id.monthTextView);
        yearTextView = findViewById(R.id.yearTextView);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // Update the TextViews with the current date
        dateTextView.setText(String.valueOf(dayOfMonth));

        // Use SimpleDateFormat to format the month as "MMM" (short month name)
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());
        String monthName = monthFormat.format(calendar.getTime());
        monthTextView.setText(monthName);

        yearTextView.setText(String.valueOf(year));

        taskList = new ArrayList<>();
         // Initialize your custom adapter

        RecyclerView todoRecyclerView = findViewById(R.id.todoRecyclerView);
        adapter = new TaskAdapter(taskList,todoRecyclerView);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoRecyclerView.setAdapter(adapter);

        // Floating Action Button (FAB)
        FloatingActionButton addTaskFAB = findViewById(R.id.addTaskFAB);
        addTaskFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskBottomSheet();
            }
        });

        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        dbHelper = new TaskDBHelper(this);
        loadTasksFromDatabase();
        adapter.setOnItemClickListener(this);
    }
    private void loadTasksFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_TITLE,
                TaskContract.TaskEntry.COLUMN_DESCRIPTION,
                TaskContract.TaskEntry.COLUMN_DUE_DATE,
                TaskContract.TaskEntry.COLUMN_PRIORITY,
                TaskContract.TaskEntry.COLUMN_CATEGORY,
                TaskContract.TaskEntry.COLUMN_STATUS
        };

        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        taskList.clear();

        while (cursor.moveToNext()) {
            int taskId = cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_DESCRIPTION));
            String dueDate = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_DUE_DATE));
            String priority = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_PRIORITY));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_CATEGORY));
            String status = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_STATUS));

            Task task = new Task(taskId,title, description, dueDate, priority, category, status);
            taskList.add(task);
        }

        cursor.close();
        adapter.notifyDataSetChanged();


    }

    private void addTaskToDatabase(Task newTask) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_TITLE, newTask.getTitle());
        values.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, newTask.getDescription());
        values.put(TaskContract.TaskEntry.COLUMN_DUE_DATE, newTask.getDueDate());
        values.put(TaskContract.TaskEntry.COLUMN_PRIORITY, newTask.getPriority());
        values.put(TaskContract.TaskEntry.COLUMN_CATEGORY, newTask.getCategory());
        values.put(TaskContract.TaskEntry.COLUMN_STATUS, newTask.getStatus());

        long newRowId = db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);

        if (newRowId != -1) {

            loadTasksFromDatabase();
        } else {

        }
    }
    @Override
    public void onItemClick(Task task) {
        if (task != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if (task.getStatus().equals("New")) {
                builder.setTitle("Task Description")
                        .setMessage(task.getDescription())
                        .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                task.setStatus("In-Progress");
                                task.setNew(false);
                                updateTaskStatusInDatabase(task); // Update status in the database
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        })
                        .show();
            } else if ("In-Progress".equals(task.getStatus())) {
                builder.setTitle("Task Description")
                        .setMessage(task.getDescription())
                        .setPositiveButton("Yes, Completed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                task.setStatus("Completed");
                                updateTaskStatusInDatabase(task); // Update status in the database
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        })
                        .show();
            }else if ("Completed".equals(task.getStatus())) {
                builder.setTitle("Task Description")
                        .setMessage(task.getDescription())
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteTaskFromDatabase(task);// Update status in the database
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        }
    }

    private void updateTaskStatusInDatabase(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_STATUS, task.getStatus());

        String selection = TaskContract.TaskEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(task.getId())};

        db.update(TaskContract.TaskEntry.TABLE_NAME, values, selection, selectionArgs);
    }
    private void deleteTaskFromDatabase(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Define the selection and selectionArgs to delete the task based on its ID
        String selection = TaskContract.TaskEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(task.getId())};

        // Delete the task from the database
        db.delete(TaskContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
        loadTasksFromDatabase();
    }


    private void showAddTaskBottomSheet() {
        AddTaskBottomSheetFragment bottomSheetFragment = new AddTaskBottomSheetFragment();
        bottomSheetFragment.setOnTaskAddedListener(this);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }


    @Override
    public void onTaskAdded(Task newTask) {
        addTaskToDatabase(newTask);
    }


}
