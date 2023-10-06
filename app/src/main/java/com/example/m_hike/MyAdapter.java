package com.example.m_hike;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<HikingData> dataList;
    DatabaseHelper dbHelper;
    MainActivity mainActivity;

    public MyAdapter(Context context, List<HikingData> dataList, DatabaseHelper dbHelper, MainActivity mainActivity) {
        this.context = context;
        this.dataList = dataList;
        this.dbHelper = dbHelper;
        this.mainActivity = mainActivity;
    }

    public void resetDataList(List<HikingData> newDataList) {
        dataList = newDataList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view, dataList, dbHelper, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewName.setText("Name: "+ dataList.get(position).getName());
        holder.textViewLocation.setText("Location: "+ dataList.get(position).getLocation());
        holder.textViewDate.setText("Date: "+ dataList.get(position).getDate());
        holder.textViewParkingAvailable.setText("Parking Available: "+ dataList.get(position).getParkingAvailable());
        holder.textViewLengthOfHike.setText("Length Of Hike: "+ dataList.get(position).getLengthOfHike());
        holder.textViewDifficultLevel.setText("Difficult Level: "+ dataList.get(position).getDifficultLevel());
        holder.textViewDescription.setText("Description: "+ dataList.get(position).getDescription());

//        holder.recCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, DetailActivity.class);
//                intent.putExtra("Name", dataList.get(holder.getAdapterPosition()).getName());
//                intent.putExtra("Location", dataList.get(holder.getAdapterPosition()).getLocation());
//                intent.putExtra("Date", dataList.get(holder.getAdapterPosition()).getDate());
//                intent.putExtra("Parking Available", dataList.get(holder.getAdapterPosition()).getParkingAvailable());
//                intent.putExtra("Length Of Hike", dataList.get(holder.getAdapterPosition()).getLengthOfHike());
//                intent.putExtra("Difficult Level", dataList.get(holder.getAdapterPosition()).getDifficultLevel());
//                intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getDescription());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<HikingData> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    TextView textViewName,textViewLocation, textViewDate, textViewParkingAvailable, textViewLengthOfHike, textViewDifficultLevel, textViewDescription;
    CardView recCard;
    Button buttonDelete, buttonUpdate;
    List<HikingData> dataList;
    DatabaseHelper dbHelper;
    MyAdapter adapter;


    public MyViewHolder(@NonNull View itemView, List<HikingData> dataList, DatabaseHelper dbHelper, MyAdapter adapter) {
        super(itemView);
        this.dataList = dataList;
        this.dbHelper = dbHelper;
        this.adapter = adapter;

        recCard = itemView.findViewById(R.id.recCard);
        textViewName = itemView.findViewById(R.id.textView_Name);
        textViewLocation = itemView.findViewById(R.id.textView_location);
        textViewDate = itemView.findViewById(R.id.textView_date);
        textViewParkingAvailable = itemView.findViewById(R.id.textView_parkingAvailable);
        textViewLengthOfHike = itemView.findViewById(R.id.textView_lengthOfHike);
        textViewDifficultLevel = itemView.findViewById(R.id.textView_difficultLevel);
        textViewDescription = itemView.findViewById(R.id.textView_description);

        // Initialize the buttons
        buttonDelete = itemView.findViewById(R.id.btnDelete);
        buttonUpdate = itemView.findViewById(R.id.btnUpdate);

        // Set click listeners for the buttons
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the Delete button click
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    HikingData hikingData = dataList.get(position);
                    int id = hikingData.getId();
                    dbHelper.deleteHikingRecord(id);

                    // Remove the deleted item from the data list
                    dataList.remove(position);

                    // Notify the adapter that the data has changed
                    adapter.resetDataList(dataList);
                }
            }
        });

//        buttonUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Handle the Update button click
//                int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    // Call a method to update the item based on the position
//                    // For example, you can use an interface/callback to communicate with the activity/fragment
//                }
//            }
//        });
    }

}