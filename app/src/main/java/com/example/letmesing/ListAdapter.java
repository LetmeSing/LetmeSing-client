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

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter {
    //  선호 목록의 customListview 를 제작하기위한 customAdapter
    private Context context;
    private List musicList;

    class ViewHolder {
        public TextView tv_song;
        public TextView tv_singer;
        public ImageView iv_delete;
    }

    public ListAdapter(Context context, ArrayList musicList) {
        super(context, 0, musicList);
        this.context = context;
        this.musicList = musicList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ListAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.favorite_item, parent, false);
        }

        viewHolder = new ListAdapter.ViewHolder();
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
                musicList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(getContext(), (position+1)+"번째 아이템이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // image 나 button 등에 Tag를 사용해서 position 을 부여해 준다.
        // Tag란 View를 식별할 수 있게 바코드 처럼 Tag를 달아 주는 View의 기능
        viewHolder.tv_song.setTag(musicItem.getSong());

        //Return the completed view to render on screen
        return convertView;
    }
}
