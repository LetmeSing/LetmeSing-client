package com.example.letmesing;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Retrofit_interface {
    // 앞에 @ 붙은건 https 의 methods 임 @POST, @PATCH...
    @GET("api/music/album/{UserID}")
        Call<DataModel> test_api_get(@Path("UserID") String userid);


    @GET("api/seat/{UserID}")
        Call<List<Place>> seat_api_get(@Path("UserID") String userid);

}
