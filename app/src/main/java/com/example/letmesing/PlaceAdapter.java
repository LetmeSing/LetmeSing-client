package com.example.letmesing;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
<<<<<<< HEAD
import android.graphics.Color;
=======
>>>>>>> 9ee6ba952327c1dfbdae339557933c0da17c0c7f
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import android.Manifest;

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

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TempPlace tempPlace = placeList.get(position);
        holder.titleTextView.setText(tempPlace.getName());
        holder.addressTextView.setText(tempPlace.getAddress());
        holder.remainingSeatView.setText("남은 자리 : " + String.valueOf(tempPlace.getRemainingSeat()));
        if(tempPlace.getRemainingSeat()<6){holder.remainingSeatView.setTextColor(Color.RED);};

        // 현재 위치 가져오기
        LocationManager locationManager = (LocationManager) holder.itemView.getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(holder.itemView.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(holder.itemView.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) holder.itemView.getContext(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        if (location != null) {
            // 마커와 현재 위치 간의 거리 계산
            float[] distance = new float[1];
            Location.distanceBetween(location.getLatitude(), location.getLongitude(), tempPlace.getLatitude(), tempPlace.getLongitude(), distance);
            float distanceInMeters = distance[0] / 1000;
            double distanceInKMeters = (Math.round(distanceInMeters * 100) /100.0);
            // 거리 표시
            holder.distanceTextView.setText(distanceInKMeters + "km");
        }

<<<<<<< HEAD
=======
        // 현재 위치 가져오기
        LocationManager locationManager = (LocationManager) holder.itemView.getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(holder.itemView.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(holder.itemView.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) holder.itemView.getContext(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        if (location != null) {
            // 마커와 현재 위치 간의 거리 계산
            float[] distance = new float[1];
            Location.distanceBetween(location.getLatitude(), location.getLongitude(), tempPlace.getLatitude(), tempPlace.getLongitude(), distance);
            float distanceInMeters = distance[0] / 1000;
            double distanceInKMeters = (Math.round(distanceInMeters * 100) /100.0);
            // 거리 표시
            holder.distanceTextView.setText(distanceInKMeters + "km");
        }
>>>>>>> 9ee6ba952327c1dfbdae339557933c0da17c0c7f
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
