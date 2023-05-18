package com.example.letmesing;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Retrofit_interface {
    // 앞에 @ 붙은건 https 의 methods 임 @POST, @PATCH...

    // DB에 저장된 Album 중 userID 에 해당하는 album 만 받아오는 함수
    @GET("api/album/{UserID}")
    Call<AlbumDM> single_album_api_get(@Path("UserID") String userid);

    // DB에 저장된 Album List 전체를 받아오는 함수
    // my_api_get() 은 특정 usr_id 가 아니라 전체 data 를 list<> 형태로 받아옴
    @GET("api/album/")
    Call<List<AlbumDM>> album_api_get();
    @GET("api/album/")
    Call<List<AlbumDM>> album_api_get(@Query("id") String id);

    @GET("api/album/music/")
    Call<List<MusicDM>> music_api_get();
    @GET("api/album/music/")
    Call<List<MusicDM>> music_api_get(@Query("album") int album);

    @POST("api/album/")
        Call<AlbumDM> album_api_post(@Body AlbumDM album);
    @POST("api/album/music/")
        Call<MusicDM> music_api_post(@Body MusicDM music);

    @GET("api/seat/")
    Call<List<TempPlace>> seat_api_get();
}