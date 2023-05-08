package com.example.letmesing;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyListFragment extends Fragment  {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Button btn_addSong;
    ArrayList<FavoriteItem> itemList;
    ListView lv_custom;
    private static CustomAdapter customAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_list, container, false);

        // data 생성 (원래는 서버에서 가져옴)
        itemList = new ArrayList<>();
        itemList.add(new FavoriteItem("노래이름1", "가수1"));
        itemList.add(new FavoriteItem("노오래이름2", "가아수2"));
        itemList.add(new FavoriteItem("노오오래이름3", "가아아수3"));
        itemList.add(new FavoriteItem("노오오오래이름4", "가아아아아수4"));
        itemList.add(new FavoriteItem("노래이름5", "가수5"));
        itemList.add(new FavoriteItem("노오래이름6", "가아수6"));
        itemList.add(new FavoriteItem("노오오래이름7", "가아아수7"));
        itemList.add(new FavoriteItem("노오오오래이름8", "가아아아아수8"));

        lv_custom = (ListView) rootView.findViewById(R.id.listview_custom); // list view 연결
        customAdapter = new CustomAdapter(getContext(), itemList); // 생성한 data 로 adapter 생성
        lv_custom.setAdapter(customAdapter); // adapter 연결

        lv_custom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),(i+1)+"번째 아이템이 클릭되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        btn_addSong = (Button) rootView.findViewById(R.id.button_add);
        btn_addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtv_song = (EditText) rootView.findViewById(R.id.editText_song);
                EditText edtv_singer = (EditText) rootView.findViewById(R.id.editText_singer);
                String song = edtv_song.getText().toString();
                String singer = edtv_singer.getText().toString();
                itemList.add(new FavoriteItem(song, singer));
                customAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "추가 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
class FavoriteItem {
    String song;
    String singer;

    public FavoriteItem (String song, String singer/*, int id*/) {
        this.song = song;
        this.singer = singer;
    }

    public String getSong() {   return this.song;   }
    public String getSinger() { return this.singer; }
    public void setSong(String song) {  this.song = song;   }
    public void setSinger(String singer) {  this.singer = singer;   }
}