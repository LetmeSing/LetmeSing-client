package com.example.letmesing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class data_model {
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
