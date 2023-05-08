package com.example.letmesing;

import com.google.android.gms.maps.model.LatLng;

public class Place {
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private int remainingSeat;
    private int totalSeat;

    public Place(String name, String address, int remainingSeat, int totalSeat, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.remainingSeat = remainingSeat;
        this.totalSeat = totalSeat;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Place(String name,  String address, int remainingSeat, int totalSeat){
        this.name = name;
        this.address = address;
        this.remainingSeat = remainingSeat;
        this.totalSeat = totalSeat;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public int getRemainingSeat(){
        return remainingSeat;
    }

    public int getTotalSeat(){
        return totalSeat;
    }
}
