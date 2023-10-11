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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public void saveData() {
        TextInputLayout textInputLayoutName = findViewById(R.id.textInputLayoutName);
        TextInputLayout textInputLayoutLocation = findViewById(R.id.textInputLayoutLocation);
        TextInputLayout textInputLayoutDate = findViewById(R.id.textInputLayoutDate);
        TextInputLayout textInputLayoutParkingAvailable = findViewById(R.id.textInputLayoutParkingAvailable);
        TextInputLayout textInputLayoutLengthOfHike = findViewById(R.id.textInputLayoutLengthOfHike);
        TextInputLayout textInputLayoutDifficultLevel = findViewById(R.id.textInputLayoutDifficultLevel);

        // Ẩn thông báo lỗi và xóa dữ liệu trong EditText
        textInputLayoutName.setErrorEnabled(false); // Tắt hiển thị lỗi
        textInputLayoutLocation.setErrorEnabled(false);
        textInputLayoutDate.setErrorEnabled(false);
        textInputLayoutParkingAvailable.setErrorEnabled(false);
        textInputLayoutLengthOfHike.setErrorEnabled(false);
        textInputLayoutDifficultLevel.setErrorEnabled(false);


        // Lấy dữ liệu từ các trường nhập
        String name = editTextName.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String parkingAvailable = editTextParkingAvailable.getText().toString().trim();
        String lengthOfHike = editTextLengthOfHike.getText().toString().trim();
        String difficultLevel = editTextDifficultLevel.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        // Kiểm tra và hiển thị thông báo lỗi nếu các trường bắt buộc không hợp lệ
        boolean isValid = true;

        if (name.isEmpty()) {
            textInputLayoutName.setError("Name is required");
            isValid = false;
        } else {
            textInputLayoutName.setError(null); // Xóa thông báo lỗi nếu trường hợp lệ
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
            // Hiển thị thông báo lỗi và không lưu dữ liệu nếu có lỗi
            Toast.makeText(UploadActivity.this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return; // Không thực hiện cập nhật nếu có lỗi
        }

        // Thực hiện thêm dữ liệu vào cơ sở dữ liệu
        long newRowId = dbHelper.insertHikingRecord(name, location, date, parkingAvailable, lengthOfHike, difficultLevel, description);

        if (newRowId != -1) {
            // Dữ liệu đã được thêm thành công
            Toast.makeText(this, "Dữ liệu đã được thêm thành công.", Toast.LENGTH_SHORT).show();

            // Gọi phương thức refreshData() trong MainActivity để cập nhật danh sách dữ liệu
            MainActivity mainActivity = (MainActivity) getParent();
            mainActivity.refreshData();

            // Đóng Activity UploadActivity và quay lại MainActivity
            finish();
        } else {
            // Xử lý lỗi khi thêm dữ liệu không thành công (ví dụ: hiển thị thông báo)
            Toast.makeText(this, "Lỗi khi thêm dữ liệu.", Toast.LENGTH_SHORT).show();
        }
    }

}