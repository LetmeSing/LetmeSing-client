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

    private List<Place> placeList;

    public PlaceAdapter(List<Place> placeList) {
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
        Place place = placeList.get(position);
        holder.titleTextView.setText(place.getName());
        holder.addressTextView.setText(place.getAddress());
        holder.remainingSeatView.setText(place.getRemainingSeat());
        //holder.placeImageView.setImageResource(place.getPhotoResId());

    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        //private ImageView placeImageView;
        private TextView titleTextView;
        private TextView addressTextView;
        private TextView remainingSeatView;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            //placeImageView = itemView.findViewById(R.id.placeImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            addressTextView = itemView.findViewById(R.id.snippetTextView);
            remainingSeatView = itemView.findViewById(R.id.remainingSeatView);
        }
    }
}
