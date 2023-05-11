package com.example.letmesing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private List<TempPlace> placeList;

    public PlaceAdapter(List<TempPlace> placeList) {
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.karaoke_list, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        TempPlace tempPlace = placeList.get(position);
        holder.titleTextView.setText(tempPlace.getName());
        holder.addressTextView.setText(tempPlace.getAddress());
        holder.remainingSeatView.setText(String.valueOf(tempPlace.getRemainingSeat()));
        //holder.placeImageView.setImageResource(place.getPhotoResId());
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView addressTextView;
        private TextView remainingSeatView;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            addressTextView = itemView.findViewById(R.id.snippetTextView);
            remainingSeatView = itemView.findViewById(R.id.remainingSeatView);
        }
    }
}
