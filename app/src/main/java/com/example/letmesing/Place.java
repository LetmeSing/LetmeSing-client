package com.example.letmesing;

import com.google.android.gms.maps.model.LatLng;

public class Place {
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private int remainingSeat;
    private int totalSeat;
    private String callNumber;
    private double star;
    private int waiting;

    public Place(String name, String address, int remainingSeat, int totalSeat, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.remainingSeat = remainingSeat;
        this.totalSeat = totalSeat;
        this.latitude = latitude;
        this.longitude = longitude;
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
