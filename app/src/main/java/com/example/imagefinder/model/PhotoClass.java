package com.example.imagefinder.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PhotoClass {

    @SerializedName("photos")
    public Photos photos;


    public static class Photos{
        @SerializedName("page")
        public int page;

        @SerializedName("pages")
        public int pages;

        @SerializedName("perpage")
        public int perpage;

        @SerializedName("total")
        public int total;

        @SerializedName("photo")
        public List<Photo> data = new ArrayList<>();
    }


    public static class Photo {
        @SerializedName("id")
        public String id;

        @SerializedName("farm")
        public String farm;

        @SerializedName("secret")
        public String secret;

        @SerializedName("server")
        public String server;

    }

}
