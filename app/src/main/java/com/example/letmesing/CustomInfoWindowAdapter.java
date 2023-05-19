package com.example.letmesing;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final Context mContext;
    private final LayoutInflater mInflater;


    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = mInflater.inflate(R.layout.custom_info_window, null);
        render(marker, view);
        return view;
    }

    private void render(Marker marker, View view) {
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView addressTextView = view.findViewById(R.id.addressTextView);
        TextView remainingSeatView = view.findViewById(R.id.remainingSeatView);
        TextView totalSeatView = view.findViewById(R.id.totalSeatView);

        // 마커 정보 설정
        String title = marker.getTitle();
        String address = marker.getSnippet();
        String remainingSeat = marker.getTag().toString();
        String totalSeat = marker.getTag().toString();

        titleTextView.setText(title);
        addressTextView.setText(address);
        remainingSeatView.setText(remainingSeat);
        totalSeatView.setText(totalSeat);
    }
}
