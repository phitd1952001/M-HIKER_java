package com.example.m_hike;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UploadActivity extends AppCompatActivity {
    Button saveButton, backButton;
    EditText editTextName, editTextLocation, editTextDate, editTextParkingAvailable, editTextLengthOfHike, editTextDifficultLevel, editTextDescription;
    DatabaseHelper dbHelper;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        editTextName = findViewById(R.id.editTextName);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextDate = findViewById(R.id.editTextDate);
        editTextParkingAvailable = findViewById(R.id.editTextParkingAvailable);
        editTextLengthOfHike = findViewById(R.id.editTextLengthOfHike);
        editTextDifficultLevel = findViewById(R.id.editTextDifficultLevel);
        editTextDescription = findViewById(R.id.editTextDescription);
        saveButton = findViewById(R.id.saveButton);
        Button backButton = findViewById(R.id.backButton);

        // Initialize your dbHelper here
        dbHelper = new DatabaseHelper(this);

        // Set the current date and time to the "editTextDate" field
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm a", Locale.getDefault());
        String currentDateTime = dateTimeFormat.format(new Date());
        editTextDate.setText(currentDateTime);

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
                        UploadActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedDate.set(Calendar.MINUTE, minute);

                                // Format and set the selected date and time to the "editTextDate" field
                                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
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
                onBackPressed(); // This will simulate the system's back button press
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
        TextInputLayout textInputLayoutLocation = findViewById(R.id.textInputLayoutLocation);
        TextInputLayout textInputLayoutDate = findViewById(R.id.textInputLayoutDate);
        TextInputLayout textInputLayoutParkingAvailable = findViewById(R.id.textInputLayoutParkingAvailable);
        TextInputLayout textInputLayoutLengthOfHike = findViewById(R.id.textInputLayoutLengthOfHike);
        TextInputLayout textInputLayoutDifficultLevel = findViewById(R.id.textInputLayoutDifficultLevel);

        // Hide error messages and clear data in EditTextt
        textInputLayoutName.setErrorEnabled(false); // Turn off error display
        textInputLayoutLocation.setErrorEnabled(false);
        textInputLayoutDate.setErrorEnabled(false);
        textInputLayoutParkingAvailable.setErrorEnabled(false);
        textInputLayoutLengthOfHike.setErrorEnabled(false);
        textInputLayoutDifficultLevel.setErrorEnabled(false);


        // Get data from input fields
        String name = editTextName.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String parkingAvailable = editTextParkingAvailable.getText().toString().trim();
        String lengthOfHike = editTextLengthOfHike.getText().toString().trim();
        String difficultLevel = editTextDifficultLevel.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        // Check and display an error message if required fields are invalid
        boolean isValid = true;

        if (name.isEmpty()) {
            textInputLayoutName.setError("Name is required");
            isValid = false;
        } else {
            textInputLayoutName.setError(null); // Clear error message if field is valid
        }

        if (location.isEmpty()) {
            textInputLayoutLocation.setError("Location is required");
            isValid = false;
        } else {
            textInputLayoutLocation.setError(null);
        }

        if (date.isEmpty()) {
            textInputLayoutDate.setError("Date is required");
            isValid = false;
        } else {
            textInputLayoutDate.setError(null);
        }

        if (parkingAvailable.isEmpty()) {
            textInputLayoutParkingAvailable.setError("Parking Available is required");
            isValid = false;
        } else {
            textInputLayoutParkingAvailable.setError(null);
        }

        if (lengthOfHike.isEmpty()) {
            textInputLayoutLengthOfHike.setError("Length Of Hike is required");
            isValid = false;
        } else {
            textInputLayoutLengthOfHike.setError(null);
        }

        if (difficultLevel.isEmpty()) {
            textInputLayoutDifficultLevel.setError("Difficult Level is required");
            isValid = false;
        } else {
            textInputLayoutDifficultLevel.setError(null);
        }

        if (!isValid) {
            // Display an error message and do not save data if there is an error
            Toast.makeText(UploadActivity.this, "Please complete all information", Toast.LENGTH_SHORT).show();
            return; // Do not perform update if there is an error
        }

        // Perform adding data to the database
        long newRowId = dbHelper.insertHikingRecord(name, location, date, parkingAvailable, lengthOfHike, difficultLevel, description);

        if (newRowId != -1) {
            // Data has been added successfully
            Toast.makeText(this, "Data was added successfully", Toast.LENGTH_SHORT).show();

            // Call the refreshData() method in MainActivity to update the data list
            MainActivity mainActivity = (MainActivity) getParent();
            mainActivity.refreshData();

            // Close Activity UploadActivity and return to MainActivity
            finish();
        } else {
            // Handle errors when adding data fails (e.g. display a message)
            Toast.makeText(this, "Error adding data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}