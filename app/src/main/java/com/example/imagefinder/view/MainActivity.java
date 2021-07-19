package com.example.imagefinder.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ProgressBar;


import com.example.imagefinder.R;
import com.example.imagefinder.viewmodel.ActivityViewModel;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity1";

    RecyclerView mRecyclerView;
    SearchView searchView;
    ProgressBar progressBar;

    private MyAdapter adapter;
    private int currentPage = 1;

    List<Bitmap> bitmapsList = new ArrayList<>();
    ActivityViewModel mViewModel;
    boolean isScrolling = false;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recycler_view);
        searchView = findViewById(R.id.search_bar);
        progressBar = findViewById(R.id.new_progressBar);


        mViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        mViewModel.getBitmapList().observe(this, new Observer<List<Bitmap>>() {
            @Override
            public void onChanged(List<Bitmap> updatedList) {
                 Log.d(TAG, "onChanged");
                    if(updatedList != null) {
                        Log.d(TAG, "updatedList Size "+ updatedList.size());
                        bitmapsList.addAll(updatedList);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
            }
        });
        intiViews();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG,"query "+query);
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                bitmapsList.clear();
                adapter.clearData();
                adapter.notifyDataSetChanged();
                mViewModel.QueryChanged();
                currentPage = 1;
                mViewModel.fetchSearchResults(query, currentPage);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void intiViews() {

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(bitmapsList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemViewCacheSize(10);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int currentItems = layoutManager.getChildCount();
                int totalItem = layoutManager.getItemCount();
                int scrolledItem = layoutManager.findFirstVisibleItemPosition();

                if (scrolledItem + currentItems >= totalItem) {
                    progressBar.setVisibility(View.VISIBLE);
                    isScrolling = false;
                    currentPage++;
                    loadMoreItems();
                }
            }
        });
    }

    private void loadMoreItems() {
             mViewModel.fetchSearchResults(searchView.getQuery().toString(),currentPage);

    }


}