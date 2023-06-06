package com.example.letmesing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

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

        titleTextView.setText(title);
        addressTextView.setText(address);
        remainingSeatView.setText(remainingSeat);
        if (Short.parseShort(remainingSeat) < 6) {
            remainingSeatView.setTextColor(Color.RED);
        }
        totalSeatView.setText(totalSeat);

        // 이미지 로딩
        new ImageLoadTask(image, imageView).execute();
    }

    private static class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
        private final String imageUrl;
        private final WeakReference<ImageView> imageViewReference;

        public ImageLoadTask(String imageUrl, ImageView imageView) {
            this.imageUrl = imageUrl;
            this.imageViewReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    // 기본 이미지 설정
                    imageView.setImageResource(R.drawable.default_image);
                }
            }
        }
    }
}