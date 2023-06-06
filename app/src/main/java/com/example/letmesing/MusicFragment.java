package com.example.letmesing;

import android.os.Build;
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

public class MusicFragment extends Fragment  {

    Button btn_addSong;
    ArrayList<MusicItem> specific_musicList; // 특정 album_id 에 해당하는 music 들의 list
    ListView lv_custom;
    String album_id = "0";
    String id_db_temp = null; // main 에서 thread 동작시키기 위해서 가져와봄 수정필요?
    private static MusicAdapter musicAdapter;

    public MusicFragment() {}
    public MusicFragment(String album_id) {
        super();
        this.album_id = album_id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_music, container, false);

        ArrayList<MusicItem> musicList = get_musicList();   // API 호출 > 전체 music List GET
        specific_musicList = pickMusic(album_id, musicList);   // 앨범에 해당하는 Music List 만 추출
        musicAdapter = new MusicAdapter(getContext(), specific_musicList); // MusicList 로 adapter 생성

        lv_custom = (ListView) rootView.findViewById(R.id.listview_custom); // list view 연결
        lv_custom.setAdapter(musicAdapter); // adapter 연결

        lv_custom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getContext(),(i+1)+"번째 노래가 클릭되었습니다.",Toast.LENGTH_SHORT).show();
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

                MusicDM musicDM = new MusicDM(song, singer, album_id);
                post_music(musicDM);

                specific_musicList = pickMusic(album_id, get_musicList());   // 앨범에 해당하는 Music List 만 추출
                musicAdapter = new MusicAdapter(getContext(), specific_musicList); // MusicList 로 adapter 생성
                lv_custom.setAdapter(musicAdapter); // adapter 연결

            }
        });

        return rootView;
    }

    private ArrayList<MusicItem> get_musicList () {
        //  API GET > DB 에 있는 전체 Music List
        ArrayList<MusicItem> musicList = new ArrayList<>();
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
//                            temp.prtAll();
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
        return musicList;
    }
    private void post_music (MusicDM musicDM) {
        // API POST > 생성된 musicDM 객체 DB 에 저장
        Call<MusicDM> callSync = RetrofitClient.getApiService().music_api_post(musicDM);
        Thread th_temp = new Thread() {
            public void run() {
                Response<MusicDM> response;
                {
                    try {
                        response = callSync.execute();
                        if (response.isSuccessful()) {
                            Log.d("연결 성공: ", response.body().toString());
                            return;
                        }
                        Log.e("연결 Code: ", Integer.toString(response.code()));
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
    }
    private ArrayList<MusicItem> pickMusic (String album, ArrayList<MusicItem> musicList) {
        ArrayList<MusicItem> result = new ArrayList<>();
        int i = 0;
        while (i < musicList.size()) {
            if (musicList.get(i).getAlbum().compareTo(album) == 0) {
                result.add(musicList.get(i));
            }
            i++;
        }
        return result;
    }

    public void setId_db_temp (String new_id) {
        this.id_db_temp = new_id;
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