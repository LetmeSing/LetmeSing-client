package com.example.letmesing;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MyListFragment extends Fragment  {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Button btn_addSong;
    ArrayList<MusicItem> musicList;
    ListView lv_custom;
    private static ListAdapter listAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_list, container, false);

        musicList = new ArrayList<>();
        Call<List<MusicDM>> callSync = RetrofitClient.getApiService().music_api_get();
        Thread th_temp = new Thread() {
            public void run() {
                Response<List<MusicDM>> response;
                {
                    try {
                        response = callSync.execute();
                        List<MusicDM> apiResponse = response.body();
                        // for (A:B) >> B 가 empty 할 때 까지 B 에서 차례대로 객체를 꺼내 A 에 넣겠다
                        for (MusicDM music:apiResponse) {
                            MusicItem temp = new MusicItem(music.getId(), music.getName(), music.getSinger(), music.getAlbum());
                            musicList.add(temp);
                            temp.prtAll();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        th_temp.start();
        try {
            th_temp.join(); // api 를 통해 data 를 받기전에 UI 가 먼저 생성되는 경우 막기 위한 join
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        /*
        musicList.add(new MusicItem("노래이름1", "가수1"));
        musicList.add(new MusicItem("노오래이름2", "가아수2"));
        musicList.add(new MusicItem("노오오래이름3", "가아아수3"));
        musicList.add(new MusicItem("노오오오래이름4", "가아아아아수4"));
        musicList.add(new MusicItem("노래이름5", "가수5"));
        musicList.add(new MusicItem("노오래이름6", "가아수6"));
        musicList.add(new MusicItem("노오오래이름7", "가아아수7"));
        musicList.add(new MusicItem("노오오오래이름8", "가아아아아수8"));*/

        lv_custom = (ListView) rootView.findViewById(R.id.listview_custom); // list view 연결
        listAdapter = new ListAdapter(getContext(), musicList); // 생성한 data 로 adapter 생성
        lv_custom.setAdapter(listAdapter); // adapter 연결

        lv_custom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),(i+1)+"번째 노래가 클릭되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        btn_addSong = (Button) rootView.findViewById(R.id.button_add);
//        추가 방식 POST 로 변경필요
       /* btn_addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtv_song = (EditText) rootView.findViewById(R.id.editText_song);
                EditText edtv_singer = (EditText) rootView.findViewById(R.id.editText_singer);
                String song = edtv_song.getText().toString();
                String singer = edtv_singer.getText().toString();
                musicList.add(new MusicItem(song, singer));
                listAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "추가 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });*/

        return rootView;
    }
}
class MusicItem {
    private String id;
    private String song;
    private String singer;
    private String album;

    public MusicItem (String id, String song, String singer, String album) {
        this.id = id;
        this.song = song;
        this.singer = singer;
        this.album = album;
    }

    public void prtAll() {
        System.out.println(id + " / " + song + " / " + singer + " / " + album);
    }
    public String getId() {   return this.id;   }
    public String getSong() {   return this.song;   }
    public String getSinger() { return this.singer; }
    public String getAlbum() { return this.album; }
}