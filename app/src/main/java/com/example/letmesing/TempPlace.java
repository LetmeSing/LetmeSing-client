package com.example.letmesing;

public class TempPlace {
    private static String name;
    private static String address;
    private double latitude;
    private double longitude;
    private static int remainingSeat;

    public TempPlace(String name, String address, double latitude, double longitude, int remainingSeat) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.remainingSeat = remainingSeat;
    }

    public static String getName() {
        return name;
    }

    public static String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public static int getRemainingSeat(){
        return remainingSeat;
    }
}





