package com.example.todolistapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todolistapp.model.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class AddTaskBottomSheetFragment extends BottomSheetDialogFragment {
    private TextInputEditText titleEditText;
    private Spinner prioritySpinner;
    private Spinner categorySpinner;

    private TextInputEditText descriptionEditText;
    private TextInputEditText dueDateEditText;
    private ImageView calendarImageView;
    private MaterialAutoCompleteTextView priorityEditText1;
    private TextInputEditText categoryEditText;
    private EditText statusEditText;
    private Button saveButton;
    private String selectedDate;

    private Calendar calendar;
    private int year, month, day;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task_bottom, container, false);

        titleEditText = view.findViewById(R.id.titleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        dueDateEditText = view.findViewById(R.id.dueDateEditText);
        calendarImageView = view.findViewById(R.id.calendarImageView);
        priorityEditText1 = view.findViewById(R.id.priorityEditText);
        categoryEditText = view.findViewById(R.id.categoryEditText);
        saveButton = view.findViewById(R.id.saveButton);


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        calendarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        prioritySpinner = view.findViewById(R.id.prioritySpinner);
        categorySpinner = view.findViewById(R.id.categorySpinner);






        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    addTaskToList();
                    dismiss();
                }
            }
        });

        return view;
    }
    private OnTaskAddedListener onTaskAddedListener;

    public interface OnTaskAddedListener {
        void onTaskAdded(Task newTask);
    }

    // Setter method to set the listener
    public void setOnTaskAddedListener(OnTaskAddedListener listener) {
        this.onTaskAddedListener = listener;
    }
    private boolean validateInput() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String dueDate = selectedDate;
        String priority = prioritySpinner.getSelectedItem().toString();
        String category = categorySpinner.getSelectedItem().toString();

        if (title.isEmpty()) {
            showSnackbar("Title cannot be empty");
            return false;
        }
        if (description.isEmpty()) {
            showSnackbar("Title cannot be empty");
            return false;
        }
        if (dueDate.isEmpty()) {
            showSnackbar("Title cannot be empty");
            return false;
        }
        if (priority.isEmpty()) {
            showSnackbar("Title cannot be empty");
            return false;
        }
        if (category.isEmpty()) {
            showSnackbar("Title cannot be empty");
            return false;
        }

        // Add similar checks for other fields as needed

        return true;
    }

    private void showSnackbar(String message) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //dueDateEditText.setText(String.format("%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year));
                        selectedDate = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year);
                        AddTaskBottomSheetFragment.this.year = year;
                        month = monthOfYear;
                        day = dayOfMonth;
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void addTaskToList() {
        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String dueDate = selectedDate;
        String priority = prioritySpinner.getSelectedItem().toString();
        String category = categorySpinner.getSelectedItem().toString();

        Task newTask = new Task(title, description, dueDate, priority, category);
        if (onTaskAddedListener != null) {
            onTaskAddedListener.onTaskAdded(newTask);
        }
    }
}
