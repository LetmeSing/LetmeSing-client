package com.example.letmesing;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicAdapter extends ArrayAdapter {
    //  선호 목록의 customListview 를 제작하기위한 customAdapter
    private Context context;
    private List musicList;

    class ViewHolder {
        public TextView tv_song;
        public TextView tv_singer;
        public ImageView iv_delete;
    }

    public MusicAdapter(Context context, ArrayList musicList) {
        super(context, 0, musicList);
        this.context = context;
        this.musicList = musicList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final MusicAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.favorite_item, parent, false);
        }

        viewHolder = new MusicAdapter.ViewHolder();
        viewHolder.tv_song = (TextView) convertView.findViewById(R.id.textView_song);
        viewHolder.tv_singer = (TextView) convertView.findViewById(R.id.textView_singer);
        viewHolder.iv_delete = (ImageView) convertView.findViewById(R.id.imageView_delete);

        final MusicItem musicItem = (MusicItem) musicList.get(position);
        viewHolder.tv_song.setText(musicItem.getSong());
        viewHolder.tv_singer.setText(musicItem.getSinger());
//        Log.v("Singer 텍뷰 넣기 전: ", (String) viewHolder.tv_song.getText());

        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), (position+1)+"번째 아이템이 삭제됩니다.", Toast.LENGTH_SHORT).show();
                String id_db = ((MusicItem) musicList.get(position)).getId();
                Log.d("DELETE: Music 확인", "UI id : " + position + "\n실제 DB id : " + id_db);
                delete_music(id_db);
                musicList.remove(position);
                notifyDataSetChanged();
            }
        });

        // image 나 button 등에 Tag를 사용해서 position 을 부여해 준다.
        // Tag란 View를 식별할 수 있게 바코드 처럼 Tag를 달아 주는 View의 기능
        viewHolder.tv_song.setTag(musicItem.getSong());

        //Return the completed view to render on screen
        return convertView;
    }
    private void delete_music (String id_db) {
        // DB 에 Music instance 삭제 함수
        // UI 에 띄워주는거는 notifyDataSetChanged(); 로 끝났으니 sync 처리할 필요 없음
        Call<Void> call = RetrofitClient.getApiService().music_api_delete(id_db);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Log.d("DELETE: Music: Not Sucess", "Not Success: code " + response.message());
                    return;
                }
                Log.d("DELETE: Music: Sucess", "Success: code " + response.code());
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("DELETE: Music: Error", "실패 " + t.getMessage());
            }
        });
    }
}
