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

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

        // Trích xuất dữ liệu cũ và đặt giá trị cho các ô input
        String name = intent.getStringExtra("name");
        String date = intent.getStringExtra("date");
        String comment = intent.getStringExtra("comment");
        observationId = intent.getIntExtra("id", 0);


        editTextName.setText(name);
        editTextDate.setText(date);
        editTextComment.setText(comment);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = dateFormat.format(new Date());
        editTextDate.setText(currentDate);

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
                editTextDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        });

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        EditText editTextName = findViewById(R.id.editText_Name);

        TextInputLayout textInputLayoutName = findViewById(R.id.textInputLayoutName);

        // Ẩn thông báo lỗi và xóa dữ liệu trong EditText
        textInputLayoutName.setErrorEnabled(false); // Tắt hiển thị lỗi

        // Lấy dữ liệu từ các trường chỉnh sửa
        String name = editTextName.getText().toString().trim();

        // Kiểm tra và hiển thị thông báo lỗi nếu các trường bắt buộc không hợp lệ
        boolean isValid = true;

        if (name.isEmpty()) {
            textInputLayoutName.setError("Name is required");
            isValid = false;
        }
        else {
            textInputLayoutName.setError(null); // Xóa thông báo lỗi nếu trường hợp lệ
        }

        if (!isValid) {
            // Hiển thị thông báo lỗi và không lưu dữ liệu nếu có lỗi
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
        dialog.dismiss();

        // Gọi phương thức refreshData() trong MainActivity để cập nhật danh sách dữ liệu
        DetailActivity detailActivity = (DetailActivity) getParent();
        detailActivity.refreshData();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
