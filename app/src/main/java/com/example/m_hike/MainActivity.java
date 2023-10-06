package com.example.m_hike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    List<HikingData> dataList;
    MyAdapter adapter;
    SearchView searchView;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setCancelable(false);
//        builder.setView(R.layout.progress_layout);
//        AlertDialog dialog = builder.create();
//        dialog.show();

        dataList = new ArrayList<>();

        // Initialize your dbHelper here
        dbHelper = new DatabaseHelper(this);


//        // Thêm 3 bản ghi dữ liệu mẫu vào bảng Hiking
//        dbHelper.insertHikingRecord("Hiking Trip 1", "Location 1", "2023-10-05", "Yes", "10 miles", "Intermediate", "A beautiful hiking trip.");
//        dbHelper.insertHikingRecord("Hiking Trip 2", "Location 2", "2023-10-06", "No", "5 miles", "Easy", "An easy and short hike.");
//        dbHelper.insertHikingRecord("Hiking Trip 3", "Location 3", "2023-10-07", "Yes", "15 miles", "Difficult", "A challenging hike with steep terrain.");

        Cursor cursor = dbHelper.getAllHikingRecords();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Extract data from the cursor for each record
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex("location"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") String parkingAvailable = cursor.getString(cursor.getColumnIndex("parkingAvailable"));
                @SuppressLint("Range") String lengthOfHike = cursor.getString(cursor.getColumnIndex("lengthOfHike"));
                @SuppressLint("Range") String difficultLevel = cursor.getString(cursor.getColumnIndex("difficultLevel"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));

                // Create a UserData object for this record
                HikingData hiking = new HikingData(id, name, location, date, parkingAvailable, lengthOfHike, difficultLevel, description);

                // Add the UserData object to the list
                dataList.add(hiking);
            }
            cursor.close(); // Don't forget to close the cursor when you're done
        }

        adapter = new MyAdapter(MainActivity.this, dataList, dbHelper, this);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });

    }
    public void searchList(String text){
        ArrayList<HikingData> searchList = new ArrayList<>();
        for (HikingData dataClass: dataList){
            if (dataClass.getName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }
}