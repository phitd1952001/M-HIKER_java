package com.example.m_hike;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    FloatingActionButton fabCreate;
    RecyclerView recyclerViewDetails;
    List<ObservationData> dataListObservation;
    MyAdapterObservation adapterObservation;
    DatabaseHelper dbHelper;
    int hikingId;
    HikingData hikingData; // Thêm biến để lưu trữ tên hiking

    @SuppressLint({"MissingInflatedId", "Range"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Get hikingId from Intent using the correct key
        Intent intent = getIntent();
        hikingId = intent.getIntExtra("hikingId", 0);

        // Initialize your dbHelper here
        dbHelper = new DatabaseHelper(this);

        // Lấy tên hiking từ cơ sở dữ liệu
        hikingData = dbHelper.getHikingRecordById(hikingId);

        // Hiển thị tên hiking lên TextView
        TextView textViewHikingName = findViewById(R.id.textViewHikingName);
        textViewHikingName.setText(hikingData.getName());

        recyclerViewDetails = findViewById(R.id.recyclerObservationView);
        fabCreate = findViewById(R.id.fabCreate);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(DetailActivity.this, 1);
        recyclerViewDetails.setLayoutManager(gridLayoutManager);

        // Thêm 3 bản ghi dữ liệu mẫu vào bảng observation
 //      dbHelper.insertObservationRecord("Sample Name 1", "2023-10-10 10:00 AM", "This is a sample comment 1.", 61);
//        dbHelper.insertObservationRecord("Sample Name 2", "2023-10-11 11:00 AM", "This is a sample comment 2.", 61);
//        dbHelper.insertObservationRecord("Sample Name 3", "2023-10-12 12:00 PM", "This is a sample comment 3.", 61);

        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataListObservation = new ArrayList<>();
        @SuppressLint("Range") int id = 0;
        Cursor cursor = dbHelper.getAllObservationRecords(hikingId);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Extract data from the cursor for each record
                id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("time"));
                @SuppressLint("Range") String comment = cursor.getString(cursor.getColumnIndex("comment"));
                @SuppressLint("Range") int hikingId = cursor.getInt(cursor.getColumnIndex("hikingId"));

                // Create a UserData object for this record
                ObservationData observationData = new ObservationData(id, name, date, comment, hikingId);

                // Add the UserData object to the list
                dataListObservation.add(observationData);
            }
            cursor.close();
            dialog.hide();
        }

        adapterObservation = new MyAdapterObservation(DetailActivity.this, dataListObservation, dbHelper, this);
        recyclerViewDetails.setAdapter(adapterObservation);

        fabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(DetailActivity.this, UploadObservationActivity.class);
                    intent.putExtra("hikingId", hikingId); // Pass the hikingId to the UploadObservationActivity
                    startActivity(intent);
            }
        });
    }

    public void onBackButtonClick(View view) {
        // Xử lý sự kiện khi nút "Back" được nhấp vào
        finish(); // Đóng DetailActivity và quay lại màn hình trước đó
    }


    //refreshData create
    @SuppressLint("Range")
    public void refreshData() {
        // Khởi tạo danh sách mới để lưu trữ dữ liệu mới
        List<ObservationData> newDataList = new ArrayList<>();
        int id = 0;
        Cursor cursor = dbHelper.getAllObservationRecords(hikingId);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Extract data from the cursor for each record
                id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("time"));
                @SuppressLint("Range") String comment = cursor.getString(cursor.getColumnIndex("comment"));
                @SuppressLint("Range") int hikingId = cursor.getInt(cursor.getColumnIndex("hikingId"));

                // Create a UserData object for this record
                ObservationData observationData = new ObservationData(id, name, date, comment, hikingId);

                // Add the UserData object to the new list
                newDataList.add(observationData);
            }
            cursor.close();
        }

        // Xóa danh sách cũ
        dataListObservation.clear();

        // Thêm tất cả dữ liệu mới vào danh sách cũ
        dataListObservation.addAll(newDataList);

        // Cập nhật RecyclerView
        adapterObservation.notifyDataSetChanged();
    }

}