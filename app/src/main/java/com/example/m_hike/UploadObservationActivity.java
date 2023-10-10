package com.example.m_hike;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UploadObservationActivity  extends AppCompatActivity {
    Button saveButton, backButton;
    EditText editTextName, editTextDate, editTextComment;
    DatabaseHelper dbHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_observation);

        editTextName = findViewById(R.id.editTextName);
        editTextDate = findViewById(R.id.editTextDate);
        editTextComment = findViewById(R.id.editTextComment);
        saveButton = findViewById(R.id.saveButton);
        Button backButton = findViewById(R.id.backButton);


        // Initialize your dbHelper here
        dbHelper = new DatabaseHelper(this);

        // Set the current date without the time to the "editTextDob" field
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = dateFormat.format(new Date());
        editTextDate.setText(currentDate);


        // initialize the datepicker with the current date
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
                // Update EditText with the selected date
                editTextDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
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


    public void saveData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadObservationActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Retrieve the hikingId from the intent
        int hikingId = getIntent().getIntExtra("hikingId", 0);

        dbHelper.insertObservationRecord(
                editTextName.getText().toString(),
                editTextDate.getText().toString(),
                editTextComment.getText().toString(),
                hikingId
                );
        dialog.dismiss();

        // Gọi phương thức refreshData() trong DetailActivity để cập nhật danh sách dữ liệu
        DetailActivity detailActivity = (DetailActivity) getParent();
        detailActivity.refreshData();

        // Khi bạn đã lưu xong, sử dụng finish() để quay lại DetailActivity
        finish();
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish(); // Finish the activity when the back button is pressed
    }
}