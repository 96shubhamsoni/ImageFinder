package com.example.imagefinder.viewmodel;

import com.example.imagefinder.model.PhotoClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static final String BASE_URL = "https://www.flickr.com/services/" ;
    private Retrofit retrofit = null;
    private Call<PhotoClass> mRequest;

    private Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public void getSearchResults(String query , int perpage, String format, int page , Callback<PhotoClass> callback){
        cancelRequest();
        mRequest = getRetrofitInstance().create(ApiEndpointInterface.class).getPhotoList(query,perpage,format ,page);
        mRequest.enqueue(callback);
    }

    public void cancelRequest(){
        if(mRequest != null)
            mRequest.cancel();
    }

}
