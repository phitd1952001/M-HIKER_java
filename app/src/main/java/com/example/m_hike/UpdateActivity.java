package com.example.m_hike;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class UpdateActivity extends AppCompatActivity {

    EditText editTextName, editTextLocation, editTextDate, editTextParkingAvailable, editTextLengthOfHike, editTextDifficultLevel, editTextDescription;
    Button updateButton, backButton;
    int id; // ID của bản ghi cần cập nhật
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        editTextName = findViewById(R.id.editText_Name);
        editTextLocation = findViewById(R.id.editText_Location);
        editTextDate = findViewById(R.id.editTextDate);
        editTextParkingAvailable = findViewById(R.id.editText_ParkingAvailable);
        editTextLengthOfHike = findViewById(R.id.editText_LengthOfHike);
        editTextDifficultLevel = findViewById(R.id.editText_DifficultLevel);
        editTextDescription = findViewById(R.id.editText_Description);

        updateButton = findViewById(R.id.UpdateButton);
        backButton = findViewById(R.id.backButton);

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
                        UpdateActivity.this,
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

        // Retrieve the ID from the Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String idString = bundle.getString("id");

            // Check if ID exists or not
            if (idString != null) {
                id = (int) Long.parseLong(idString);

                // Use DatabaseHelper to get old data from the database
                HikingData hikingData = dbHelper.getHikingRecordById(id);
                if (hikingData != null) {
                    // Đặt dữ liệu cũ vào các EditText
                    editTextName.setText(hikingData.getName());
                    editTextLocation.setText(hikingData.getLocation());
                    editTextDate.setText(hikingData.getDate());
                    editTextParkingAvailable.setText(hikingData.getParkingAvailable());
                    editTextLengthOfHike.setText(hikingData.getLengthOfHike());
                    editTextDifficultLevel.setText(hikingData.getDifficultLevel());
                    editTextDescription.setText(hikingData.getDescription());
                }
            } else {
                // ID does not exist, handle optionally (e.g. show notification)
            }
        }

        // Set up click listeners for your buttons
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Close the update operation and return to the previous screen
            }
        });
    }

    private void updateData() {
        TextInputLayout textInputLayoutName = findViewById(R.id.textInputLayoutName);
        TextInputLayout textInputLayoutLocation = findViewById(R.id.textInputLayoutLocation);
        TextInputLayout textInputLayoutDate = findViewById(R.id.textInputLayoutDate);
        TextInputLayout textInputLayoutParkingAvailable = findViewById(R.id.textInputLayoutParkingAvailable);
        TextInputLayout textInputLayoutLengthOfHike = findViewById(R.id.textInputLayoutLengthOfHike);
        TextInputLayout textInputLayoutDifficultLevel = findViewById(R.id.textInputLayoutDifficultLevel);


        // Hide error messages and clear data in EditText
        textInputLayoutName.setErrorEnabled(false);
        textInputLayoutLocation.setErrorEnabled(false);
        textInputLayoutDate.setErrorEnabled(false);
        textInputLayoutParkingAvailable.setErrorEnabled(false);
        textInputLayoutLengthOfHike.setErrorEnabled(false);
        textInputLayoutDifficultLevel.setErrorEnabled(false);


        // Get data from edit fields
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
            Toast.makeText(UpdateActivity.this, "Please complete all information", Toast.LENGTH_SHORT).show();
            return; // Do not perform update if there is an error
        }

        // Continue performing "Update" if the data is valid
        // Make sure you have a valid ID to update
        if (id > 0) {
            // Perform data updates in the database
            int rowsUpdated = dbHelper.updateHikingRecord(id, name, location, date, parkingAvailable, lengthOfHike, difficultLevel, description);
            if (rowsUpdated > 0) {
                Toast.makeText(UpdateActivity.this, "Data has been updated", Toast.LENGTH_SHORT).show();

                // Call the refreshData() method in MainActivity to update the data list
                MainActivity mainActivity = (MainActivity) getParent();
                mainActivity.refreshData();

                setResult(RESULT_OK); // Set the result to success
                finish(); // End the update operation
            } else {
                Toast.makeText(UpdateActivity.this, "Error updating data", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle when there is no valid ID (e.g. display a message)
        }
    }

}
