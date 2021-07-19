package com.example.imagefinder.view;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagefinder.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MyAdapter";
    private List<Bitmap> mList;

    public MyAdapter(List<Bitmap> mList) {
        Log.d(TAG, "mList" + mList.size());
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        return new DataItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        Bitmap item = mList.get(position);
        ((DataItem) holder).imageView.setImageBitmap(item);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void clearData() {
        mList.clear();
        notifyDataSetChanged();
    }

    private class DataItem extends RecyclerView.ViewHolder {
        ImageView imageView;

        public DataItem(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }

}
