package com.example.imagefinder.viewmodel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.imagefinder.model.PhotoClass;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityViewModel extends ViewModel {

    private static final String TAG = "ActivityViewModel";
    private RetrofitService mService = new RetrofitService();

    private MutableLiveData<List<Bitmap>> mutableLiveData = new MutableLiveData<>();
    private List<Bitmap> bitmapList = new ArrayList<>();
    private PhotoClass.Photos mSearchResults;

    public LiveData<List<Bitmap>> getBitmapList() {
        mutableLiveData.setValue(bitmapList);
        return mutableLiveData;

    }

    public void fetchSearchResults(String query,int page) {
        Log.d(TAG,"fetchSearchResults");
        //ApiCall
        fetchPhotos(query,page);
    }

        private void fetchPhotos(String query,int page) {
        Log.d(TAG,"fetchPhotos");
        mService.getSearchResults(query, 9, "json", page, new Callback<PhotoClass>() {
            @Override
            public void onResponse(Call<PhotoClass> call, Response<PhotoClass> response) {
                if (response.isSuccessful() && response.body() != null && response.body().photos!=null && response.body().photos.data!=null) {
                    PhotoClass photoClass = response.body();
                    List<PhotoClass.Photo> list;
                    list = photoClass.photos.data;
                    Log.d(TAG, "listSize "+list.size() + "");
                    int i;
                    for (i = 0; i < list.size(); i++) {
                        String farm = list.get(i).farm;
                        String id = list.get(i).id;
                        String secret = list.get(i).secret;
                        String server = list.get(i).server;
                        String url = "https://live.staticflickr.com/"+server+"/"+id+"_"+secret+"_c.jpg";

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                LoadPhotoToList(url);
                                mutableLiveData.postValue(bitmapList);
                            }
                        }).start();

                    }
                }
            }

            @Override
            public void onFailure(Call<PhotoClass> call, Throwable t) {
               // mutableLiveData.postValue(null);
            }
        });

    }



    public void LoadPhotoToList(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.d(TAG,"bitmap size" + myBitmap.getByteCount());
            bitmapList.add(myBitmap);
            Log.d(TAG , "now bitmapList size "+bitmapList.size());

        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }
    }

    @Override
    protected void onCleared() {
        bitmapList.clear();
        mutableLiveData.postValue(bitmapList);
    }

    public void QueryChanged(){
        bitmapList.clear();
    }
}
