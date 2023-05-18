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