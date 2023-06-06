package com.example.letmesing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlbumDM {
    //    Album 의 DataModel
//    serializer db 에 저장된 data 는 row 형태 > 1번 row 를 뽑아줄때 byte 의 나열로 제공됨 > serializer 는 해당 DB 의 row 를 json 형태로 가공하는 method
//    deSerializer > 반대 작업
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("numOfSongs")
    @Expose
    private String numOfSongs;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("member")
    @Expose
    private String member;

    public AlbumDM () {}
    public AlbumDM (String name, String created_at, String numOfSongs, String description, String member) {
        this.name = name;
        this.created_at = created_at;
        this.numOfSongs = numOfSongs;
        this.description = description;
        this.member = member;
    }
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getCreated_at(){
        return created_at;
    }
    public String getNumOfSongs(){
        return numOfSongs;
    }
    public String getDescription(){
        return description;
    }
    public String getMember(){
        return member;
    }
}

class MusicDM {
    //    Music의 DataModel
    @SerializedName("music_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("singer")
    @Expose
    private String singer;
    @SerializedName("album")
    @Expose
    private String album;

    public MusicDM () {}
    public MusicDM (String name, String singer, String album) {
        this.name = name;
        this.singer = singer;
        this.album = album;
    }

    public String getId(){
        return id;
    }
    public String getName(){  return name;  }
    public String getSinger(){
        return singer;
    }
    public String getAlbum(){
        return album;
    }
}

class KaraokeDM {
    //    serializer db 에 저장된 data 는 row 형태 > 1번 row 를 뽑아줄때 byte 의 나열로 제공됨 > serializer 는 해당 DB 의 row 를 json 형태로 가공하는 method
    //    deSerializer > 반대 작업
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("remainingSeat")
    @Expose
    private int remainingSeat;
    @SerializedName("totalSeat")
    @Expose
    private int totalSeat;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("memberId")
    @Expose
    private String memberId;

    public KaraokeDM() {
    }

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getAddress(){
        return address;
    }
    public int getRemainingSeat(){  return remainingSeat;   }
    public int getTotalSeat(){
        return totalSeat;
    }
    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
    public String getMemberId(){    return memberId;    }
}

class TempDM {
    @SerializedName("remainingSeat")
    @Expose
    private String remainingSeat;

    public TempDM () {}
    public TempDM (String remainingSeat) {
        this.remainingSeat = remainingSeat;
    }

    public String getLoginid() {
        return remainingSeat;
    }
}

class LoginDM {
    @SerializedName("login_id")
    @Expose
    private String login_id;

    @SerializedName("password")
    @Expose
    private String password;

    public LoginDM () {}
    public LoginDM (String login_id, String password) {
        this.login_id = login_id;
        this.password = password;
    }

    public String getLoginid() {
        return login_id;
    }
    public String getPassword() {
        return password;
    }
}

class RegisterDM {
    @SerializedName("login_id")
    @Expose
    private String login_id;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("nickname")
    @Expose
    private String nickname;

    @SerializedName("is_master")
    @Expose
    private String is_master;   // 1: master, 0: normal user

    public RegisterDM () {}
    public RegisterDM (String login_id, String password, String nickname, String is_master) {
        this.login_id = login_id;
        this.password = password;
        this.nickname = nickname;
        this.is_master = is_master;
    }

    public String getLogin_id() {
        return login_id;
    }
    public String getPassword() {
        return password;
    }
    public String getNickname() {
        return nickname;
    }
    public String getIs_master() {
        return is_master;
    }
}