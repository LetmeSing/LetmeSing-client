package com.example.letmesing;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private List<TempPlace> placeList;
    private OnItemClickListener itemClickListener;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int ACCESS_FINE_LOCATION = 2;
    private static final int ACCESS_COARSE_LOCATION = 3;

    public PlaceAdapter(List<TempPlace> placeList) {
        this.placeList = placeList;
    }

    public void setPlaceList(List<TempPlace> placeList) {
        this.placeList = placeList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, TempPlace place);
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.karaoke_list, parent, false);
        return new PlaceViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TempPlace tempPlace = placeList.get(position);
        holder.titleTextView.setText(tempPlace.getName());
        holder.addressTextView.setText(tempPlace.getAddress());
        holder.remainingSeatView.setText("남은 자리 : " + String.valueOf(tempPlace.getRemainingSeat()));
        if(tempPlace.getRemainingSeat()<6){holder.remainingSeatView.setTextColor(Color.parseColor("#CD5D81"));};

        // 현재 위치 가져오기
        LocationManager locationManager = (LocationManager) holder.itemView.getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        // 위치 권한 확인
        if (ActivityCompat.checkSelfPermission(holder.itemView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(holder.itemView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(holder.itemView.getContext(), "위치정보 없음", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions((Activity) holder.itemView.getContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            // 위치 정보를 가져올 수 있는 경우
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // 마커와 현재 위치 간의 거리 계산
                    float[] distance = new float[1];
                    Location.distanceBetween(location.getLatitude(), location.getLongitude(), tempPlace.getLatitude(), tempPlace.getLongitude(), distance);
                    float distanceInMeters = distance[0] / 1000;
                    double distanceInKMeters = (Math.round(distanceInMeters * 100) / 100.0);
                    // 거리 표시
                    holder.distanceTextView.setText(distanceInKMeters + "km");
                    holder.distanceTextView.setTextColor(Color.parseColor("#D5B9B2"));
                    // 위치 업데이트가 끝나면 리스너 제거
                    locationManager.removeUpdates(this);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}

                @Override
                public void onProviderEnabled(String provider) {}

                @Override
                public void onProviderDisabled(String provider) {}
            };
            // 위치 업데이트 요청
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(position, tempPlace);

                    // Request location permission
                    if (ActivityCompat.checkSelfPermission(holder.itemView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(holder.itemView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) holder.itemView.getContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView addressTextView;
        private TextView remainingSeatView;
        private TextView distanceTextView;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            addressTextView = itemView.findViewById(R.id.snippetTextView);
            remainingSeatView = itemView.findViewById(R.id.remainingSeatView);
            distanceTextView = itemView.findViewById(R.id.distanceTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                        itemClickListener.onItemClick(position, placeList.get(position));
                    }
                }
            });
        }
    }
}
