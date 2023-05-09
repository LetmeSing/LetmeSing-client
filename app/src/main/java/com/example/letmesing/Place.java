package com.example.letmesing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Place {
    //    serializer db 에 저장된 data 는 row 형태 > 1번 row 를 뽑아줄때 byte 의 나열로 제공됨 > serializer 는 해당 DB 의 row 를 json 형태로 가공하는 method
//    deSerializer > 반대 작업
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("remaingSeat")
    @Expose
    private int remainingSeat;
    @SerializedName("totalSeat")
    @Expose
    private int totalSeat;
    @SerializedName("callNumber")
    @Expose
    private String callNumber;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("star")
    @Expose
    private double star;
    @SerializedName("waiting")
    @Expose
    private int waiting;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getAddress(){
        return address;
    }
    public String getCallNumber(){
        return callNumber;
    }
    public int getRemainingSeat(){
        return remainingSeat;
    }
    public int getTotalSeat(){
        return totalSeat;
    }
    public double getStar(){
        return star;
    }
    public int getWaiting(){
        return waiting;
    }
    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
}