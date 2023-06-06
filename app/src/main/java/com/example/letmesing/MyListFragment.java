package com.example.letmesing;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyListFragment extends Fragment  {

    public MyListFragment() {}
    public MyListFragment(String album_id) {
        super();
        this.album_id = album_id;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Button btn_addSong;
    ArrayList<MusicItem> musicList;
    ArrayList<MusicItem> specific_musicList;
    ListView lv_custom;
    String album_id = "0";
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

        specific_musicList = pickMusic(this.album_id);

        lv_custom = (ListView) rootView.findViewById(R.id.listview_custom); // list view 연결
        listAdapter = new ListAdapter(getContext(), specific_musicList); // 생성한 data 로 adapter 생성
        lv_custom.setAdapter(listAdapter); // adapter 연결

        lv_custom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),(i+1)+"번째 노래가 클릭되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        btn_addSong = (Button) rootView.findViewById(R.id.button_add);
//        추가 방식 POST 로 변경필요
        btn_addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtv_song = (EditText) rootView.findViewById(R.id.editText_song);
                EditText edtv_singer = (EditText) rootView.findViewById(R.id.editText_singer);
                String song = edtv_song.getText().toString();
                String singer = edtv_singer.getText().toString();
                // id 가 없는 상태로 저장된 item 이니 좋지 않음. 없애고 frag 를 refresh 하도록 만들것
//                specific_musicList.add(new MusicItem(song, singer, album_id));
//                listAdapter.notifyDataSetChanged();
//                Toast.makeText(getContext(), "추가 되었습니다.", Toast.LENGTH_SHORT).show();

                MusicDM musicDM = new MusicDM(song, singer, album_id);
                Call<MusicDM> call2 = RetrofitClient.getApiService().music_api_post(musicDM);
                call2.enqueue(new Callback<MusicDM>() {
                    @Override
                    public void onResponse(Call<MusicDM> call, Response<MusicDM> response) {
                        if (!response.isSuccessful()) {
                            Log.e("연결 비정상", Integer.toString(response.code()));
                            return;
                        }
                        MusicDM musicResponse = response.body();
                        Log.d("연결 성공", response.body().toString());
                    }
                    @Override
                    public void onFailure(Call<MusicDM> call, Throwable t) {
                        Log.v("연결 실패", t.getMessage());
                    }
                });
                // 강제 refresh. 임시용이라 수정 필요함
//                refresh();
            }
        });

        return rootView;
    }

    private ArrayList<MusicItem> pickMusic (String album) {
        ArrayList<MusicItem> result = new ArrayList<>();
        int i = 0;
        while (i < musicList.size()) {
            if (musicList.get(i).getAlbum().compareTo(album) == 0) {
                result.add(musicList.get(i));
                refresh();
            }
            i++;
        }
        return result;
    }
    private void refresh () {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(this).attach(this).commit();
    }
}
class MusicItem {
    private String id;
    private String song;
    private String singer;
    private String album;

    public MusicItem (String song, String singer, String album) {
//        POST 용 생성자. P-key 인 id 필드를 비워둠
        this.song = song;
        this.singer = singer;
        this.album = album;
    }
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