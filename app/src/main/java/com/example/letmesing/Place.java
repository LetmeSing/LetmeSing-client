package com.example.letmesing;

import com.google.android.gms.maps.model.LatLng;

public class Place {
    private String name;
    private String address;
    private int photoResId;
    private LatLng latLng;

    public Place(String name, String address, int photoResId, LatLng latLng) {
        this.name = name;
        this.address = address;
        this.photoResId = photoResId;
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getPhotoResId() {
        return photoResId;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
