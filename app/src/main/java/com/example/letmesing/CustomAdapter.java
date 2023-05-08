package com.example.letmesing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter {
//  선호 목록의 customListview 를 제작하기위한 customAdapter
    private Context context;
    private List list;

    class ViewHolder {
        public TextView tv_song;
        public TextView tv_singer;
        public ImageView iv_delete;
    }

    public CustomAdapter (Context context, ArrayList list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.favorite_item, parent, false);
        }

        viewHolder = new ViewHolder();
        viewHolder.tv_song = (TextView) convertView.findViewById(R.id.textView_song);
        viewHolder.tv_singer = (TextView) convertView.findViewById(R.id.textView_singer);
        viewHolder.iv_delete = (ImageView) convertView.findViewById(R.id.imageView_delete);

        final FavoriteItem fvitem = (FavoriteItem) list.get(position);
        viewHolder.tv_song.setText(fvitem.getSong());
        viewHolder.tv_singer.setText(fvitem.getSinger());

        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                notifyDataSetChanged();
                Toast.makeText(getContext(), (position+1)+"번째 아이템이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // image 나 button 등에 Tag를 사용해서 position 을 부여해 준다.
        // Tag란 View를 식별할 수 있게 바코드 처럼 Tag를 달아 주는 View의 기능
        viewHolder.tv_song.setTag(fvitem.getSong());

        //Return the completed view to render on screen
        return convertView;
    }
}
