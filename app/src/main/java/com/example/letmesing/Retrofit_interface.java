package com.example.letmesing;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
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

    @POST("api/accounts/register/")
    Call<RegisterDM> register_api_post(@Body RegisterDM signin);

    // https://th-biglight.tistory.com/11
    // https://kyome.tistory.com/148
    // Service 는 Call<T> 를 반환. 서버 API 가 String 을 반환한다면, 클라이언트는 Retrofit 을 통해 Call<String> 을 받게됨
    // 여기서는 JSon 의 형태로 반환 받음. Object 의 형태로 받아서 Gson 으로 toJson 함수로 가공할 것임.
    @POST("api/accounts/login/")
    Call<Object> login_api_post(@Body LoginDM login);

    // karaoke 전부 받는 GET
    @GET("api/seat/")
    Call<List<KaraokeDM>> karaoke_api_get();

    @FormUrlEncoded
    @PATCH("api/seat/{KaraokeID}/")
    Call<KaraokeDM> karaoke_api_patch(@Path("KaraokeID") String karaoke_id, @Field("remainingSeat") int remainingSeat);

    @GET("api/seat/")
    Call<List<TempPlace>> seat_api_get();
}