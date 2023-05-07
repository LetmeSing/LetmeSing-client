package com.example.letmesing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class retrofit_client {
//    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private static final String BASE_URL = "http://letmesing.kro.kr:8000/api/music/album/";

    public static Retrofit_interface getApiService(){   return getInstance().create(Retrofit_interface.class);  }

    private static Retrofit getInstance(){
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
