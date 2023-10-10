package com.example.m_hike;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

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

        // Retrieve the ID from the Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String idString = bundle.getString("id");

            // Kiểm tra xem ID có tồn tại hay không
            if (idString != null) {
                id = (int) Long.parseLong(idString);

                // Sử dụng DatabaseHelper để lấy dữ liệu cũ từ cơ sở dữ liệu
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
                // ID không tồn tại, xử lý tùy ý (ví dụ: hiển thị thông báo)
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
                finish(); // Đóng hoạt động cập nhật và quay lại màn hình trước
            }
        });
    }

    private void updateData() {
        EditText editTextName = findViewById(R.id.editText_Name);
        EditText editTextLocation = findViewById(R.id.editText_Location);
        EditText editTextParkingAvailable = findViewById(R.id.editText_ParkingAvailable);
        EditText editTextLengthOfHike = findViewById(R.id.editText_LengthOfHike);
        EditText editTextDifficultLevel = findViewById(R.id.editText_DifficultLevel);

        TextInputLayout textInputLayoutName = findViewById(R.id.textInputLayoutName);
        TextInputLayout textInputLayoutLocation = findViewById(R.id.textInputLayoutLocation);
        TextInputLayout textInputLayoutParkingAvailable = findViewById(R.id.textInputLayoutParkingAvailable);
        TextInputLayout textInputLayoutLengthOfHike = findViewById(R.id.textInputLayoutLengthOfHike);
        TextInputLayout textInputLayoutDifficultLevel = findViewById(R.id.textInputLayoutDifficultLevel);

        // Ẩn thông báo lỗi và xóa dữ liệu trong EditText
        textInputLayoutName.setErrorEnabled(false); // Tắt hiển thị lỗi
        textInputLayoutLocation.setErrorEnabled(false); // Tắt hiển thị lỗi
        textInputLayoutParkingAvailable.setErrorEnabled(false); // Tắt hiển thị lỗi
        textInputLayoutLengthOfHike.setErrorEnabled(false); // Tắt hiển thị lỗi
        textInputLayoutDifficultLevel.setErrorEnabled(false); // Tắt hiển thị lỗi

        // Lấy dữ liệu từ các trường chỉnh sửa
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
        }
        if (location.isEmpty()) {
            textInputLayoutLocation.setError("Location is required");
            isValid = false;
        }
        if (parkingAvailable.isEmpty()) {
            textInputLayoutParkingAvailable.setError("Parking Available is required");
            isValid = false;
        }
        if (lengthOfHike.isEmpty()) {
            textInputLayoutLengthOfHike.setError("Length Of Hike is required");
            isValid = false;
        }
        if (difficultLevel.isEmpty()) {
            textInputLayoutDifficultLevel.setError("Difficult Level is required");
            isValid = false;
        }
        else {
            textInputLayoutName.setError(null); // Xóa thông báo lỗi nếu trường hợp lệ
        }

        if (!isValid) {
            // Hiển thị thông báo lỗi và không lưu dữ liệu nếu có lỗi
            return;
        }

        // Đảm bảo bạn có ID hợp lệ để cập nhật
        if (id > 0) {
            // Thực hiện cập nhật dữ liệu trong cơ sở dữ liệu
            int rowsUpdated = dbHelper.updateHikingRecord(id, name, location, date, parkingAvailable, lengthOfHike, difficultLevel, description);
            if (rowsUpdated > 0) {
                Toast.makeText(UpdateActivity.this, "Dữ liệu đã được cập nhật.", Toast.LENGTH_SHORT).show();

                // Gọi phương thức refreshData() trong MainActivity để cập nhật danh sách dữ liệu
                MainActivity mainActivity = (MainActivity) getParent();
                mainActivity.refreshData();

                setResult(RESULT_OK); // Đặt kết quả là thành công
                finish(); // Kết thúc hoạt động cập nhật
            } else {
                Toast.makeText(UpdateActivity.this, "Lỗi khi cập nhật dữ liệu.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Xử lý khi không có ID hợp lệ (ví dụ: hiển thị thông báo)
        }
    }
}
