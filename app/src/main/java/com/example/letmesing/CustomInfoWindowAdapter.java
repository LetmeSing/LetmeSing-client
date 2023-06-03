package com.example.letmesing;

import static java.lang.Short.parseShort;
import static java.lang.Short.valueOf;

import android.annotation.SuppressLint;
import android.content.Context;
<<<<<<< HEAD
import android.graphics.Color;
=======
>>>>>>> 9ee6ba952327c1dfbdae339557933c0da17c0c7f
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final Context mContext;
    private final LayoutInflater mInflater;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // 기본 InfoWindow를 사용하도록 null 반환
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = mInflater.inflate(R.layout.custom_info_window, null);
        render(marker, view);
        return view;
    }

    @SuppressLint("ResourceAsColor")
    private void render(Marker marker, View view) {
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView addressTextView = view.findViewById(R.id.addressTextView);
        TextView remainingSeatView = view.findViewById(R.id.remainingSeatView);
        TextView totalSeatView = view.findViewById(R.id.totalSeatView);

        // 마커 정보 설정
        String title = marker.getTitle();
        String address = marker.getSnippet();
        String remainingSeat = "";
        String totalSeat = "";

        Object tag = marker.getTag();
        if (tag != null) {
            if (tag instanceof TempPlace) {
                TempPlace place = (TempPlace) tag;
                remainingSeat = String.valueOf(place.getRemainingSeat());
                totalSeat = String.valueOf(place.getTotalSeat());
            } else {
                // 태그가 다른 형식일 경우에 대한 처리
            }
        }

        titleTextView.setText(title);
        addressTextView.setText(address);
        remainingSeatView.setText(remainingSeat);
        if(parseShort(remainingSeat) < 6){remainingSeatView.setTextColor(Color.RED);}
        totalSeatView.setText(totalSeat);
    }
}