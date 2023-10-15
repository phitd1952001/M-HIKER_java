package com.example.m_hike;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class UpdateObservationActivity extends AppCompatActivity {
    Button saveButton, backButton;
    EditText editTextName, editTextDate, editTextComment;
    DatabaseHelper dbHelper;
    int hikingId;
    int observationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_observation);

        Intent intent = getIntent();
        hikingId = intent.getIntExtra("hikingId", 0);

        editTextName = findViewById(R.id.editText_Name);
        editTextDate = findViewById(R.id.editTextDate);
        editTextComment = findViewById(R.id.editText_Comment);
        saveButton = findViewById(R.id.updateButton);
        backButton = findViewById(R.id.backButton);

        dbHelper = new DatabaseHelper(this);

        // Extract old data and set values for input cells
        String name = intent.getStringExtra("name");
        String date = intent.getStringExtra("date");
        String comment = intent.getStringExtra("comment");
        observationId = intent.getIntExtra("id", 0);


        editTextName.setText(name);

        // Parse the old date string and set it in the correct format
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm a", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm a", Locale.getDefault());

        try {
            Date oldDate = inputFormat.parse(date);
            date = outputFormat.format(oldDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        editTextDate.setText(date);
        editTextComment.setText(comment);

        // Initialize the DatePickerDialog with the current date
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, null, currentYear, currentMonth, currentDay
        );

        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Set the date in a Calendar instance
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                // Initialize a TimePickerDialog for selecting the time
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        UpdateObservationActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedDate.set(Calendar.MINUTE, minute);

                                // Format and set the selected date and time to the "editTextDate" field
                                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm a", Locale.getDefault());
                                String selectedDateTime = dateTimeFormat.format(selectedDate.getTime());
                                editTextDate.setText(selectedDateTime);
                            }
                        },
                        currentHour,
                        currentMinute,
                        false
                );
                timePickerDialog.show();
            }
        });

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the DatePicker when the EditText is clicked
                datePickerDialog.show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    public void saveData() {
        TextInputLayout textInputLayoutName = findViewById(R.id.textInputLayoutName);
        TextInputLayout textInputLayoutDate = findViewById(R.id.textInputLayoutDate);

        // Hidden error notifications and data deletion in EditText
        textInputLayoutName.setErrorEnabled(false);
        textInputLayoutDate.setErrorEnabled(false);

        String name = editTextName.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();

        // Check and display error notifications if the mandatory schools are invalid
        boolean isValid = true;

        if (name.isEmpty()) {
            textInputLayoutName.setError("Name is required");
            isValid = false;
        } else {
            textInputLayoutName.setError(null); // Get data from import fields
        }

        if (date.isEmpty()) {
            textInputLayoutDate.setError("Date is required");
            isValid = false;
        } else {
            textInputLayoutDate.setError(null);
        }

        if (!isValid) {
            // Display error notifications and not save data if there is an error
            Toast.makeText(UpdateObservationActivity.this, "Please complete all information", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateObservationActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();


        dbHelper.updateObservationRecord(
                observationId,
                editTextName.getText().toString(),
                editTextDate.getText().toString(),
                editTextComment.getText().toString(),
                hikingId
                );

        // Call the refreshData() method in MainActivity to update the data list
        DetailActivity detailActivity = (DetailActivity) getParent();
        detailActivity.refreshData();
        dialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
