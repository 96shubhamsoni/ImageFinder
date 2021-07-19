package com.example.imagefinder.viewmodel;

import com.example.imagefinder.model.PhotoClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiEndpointInterface {
    String API_KEY = "062a6c0c49e4de1d78497d13a7dbb360";

    @GET("rest/?method=flickr.photos.search&api_key="+API_KEY)
    Call<PhotoClass> getPhotoList(@Query("tags") String text, @Query("per_page")int perPage, @Query("format") String format,@Query("nojsoncallback") int p);

}
