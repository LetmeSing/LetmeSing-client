package com.example.letmesing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
        ImageView imageView = view.findViewById(R.id.imageView);

        // 마커 정보 설정
        String title = marker.getTitle();
        String address = marker.getSnippet();
        String remainingSeat = "";
        String totalSeat = "";
        String image = "";

        Object tag = marker.getTag();
        if (tag != null && tag instanceof TempPlace) {
            TempPlace place = (TempPlace) tag;
            remainingSeat = String.valueOf(place.getRemainingSeat());
            totalSeat = String.valueOf(place.getTotalSeat());
            image = String.valueOf(place.getImage());
        }

        Glide.with(mContext).load(image).placeholder(R.drawable.place_holder).diskCacheStrategy(DiskCacheStrategy.ALL).fallback(R.drawable.empty).error(R.drawable.fail).into(imageView);

        titleTextView.setText(title);
        addressTextView.setText(address);
        remainingSeatView.setText(remainingSeat);
        if (Short.parseShort(remainingSeat) < 6) {
            remainingSeatView.setTextColor(R.color.red_gray);
        }
        else {
            remainingSeatView.setTextColor(Color.BLACK);
        }
        totalSeatView.setText(totalSeat);
        totalSeatView.setTextColor(Color.BLACK);

        TextView remainingSeatLabel = view.findViewById(R.id.remainingSeat);
        TextView totalSeatLabel = view.findViewById(R.id.totalSeat);

        remainingSeatLabel.setText("현재");
        remainingSeatLabel.setTextColor(R.color.gray);
        totalSeatLabel.setText("전체");
        totalSeatLabel.setTextColor(R.color.gray);
    }

}