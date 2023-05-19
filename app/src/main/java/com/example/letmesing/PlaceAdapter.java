package com.example.letmesing;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private List<TempPlace> placeList;

    public PlaceAdapter(List<TempPlace> placeList) {
        this.placeList = placeList;
    }

    public void setPlaceList(List<TempPlace> placeList) {
        this.placeList = placeList;
    }
    private OnItemClickListener itemClickListener;

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
        //holder.totalSeatView.setText(String.valueOf(tempPlace.getTotalSeat()) + "자리");
        holder.remainingSeatView.setText("남은 자리 : " + String.valueOf(tempPlace.getRemainingSeat()));

        //holder.placeImageView.setImageResource(place.getPhotoResId());
        // 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(position, tempPlace);
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
        //private TextView totalSeatView;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            addressTextView = itemView.findViewById(R.id.snippetTextView);
            //totalSeatView = itemView.findViewById(R.id.totalSeatView);
            remainingSeatView = itemView.findViewById(R.id.remainingSeatView);

        }
    }


}
