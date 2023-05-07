package com.example.letmesing;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Retrofit_interface {
    @GET("posts/{UserID}")
    Call<data_model> test_api_get(
            @Path("UserID") String userid);
}
