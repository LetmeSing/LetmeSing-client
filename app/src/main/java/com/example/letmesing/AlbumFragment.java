package com.example.letmesing;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumFragment extends Fragment {
    Button btn_addAlbum;
    ArrayList<AlbumItem> albumList;
    ListView lv_album;
    private static AlbumAdapter albumAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_album, container, false);

        albumList = new ArrayList<AlbumItem>();

        Call<List<DataModel>> callSync = RetrofitClient.getApiService().my_api_get();
        // api 의 동기적 처리를 위한 임시 thread 생성    Main thread 내에서는 네트워크 통신이 막혀있음 (thread 없이 단순 try-catch 로는 네트워크 통신 사용 불가)
        Thread th_temp = new Thread() {
            public void run() {
                Response<List<DataModel>> response;
                {
                    try {
                        response = callSync.execute();
                        List<DataModel> apiResponse = response.body();
                        // for (A:B) >> B 가 empty 할 때 까지 B 에서 차례대로 객체를 꺼내 A 에 넣겠다
                        for (DataModel album:apiResponse) {
                            AlbumItem temp = new AlbumItem(album.getId(), album.getName(), album.getCreated_at(), album.getNumOfSongs(), album.getDescription(), album.getMember());
                            albumList.add(temp);
                            Log.v("OnResponse 내부: ", Integer.toString(albumList.size()));
                        }
                    } catch (IOException e) {
                        Log.v("callAsync", "Exception");
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

        lv_album = (ListView) rootView.findViewById(R.id.listview_album); // list view 연결
        albumAdapter = new AlbumAdapter(getContext(), albumList, this); // 생성한 data 로 adapter 생성
        lv_album.setAdapter(albumAdapter); // adapter 연결

        btn_addAlbum = (Button) rootView.findViewById(R.id.button_addAlbum);
        btn_addAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Add New Album Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }
}

class AlbumItem {
    String id;
    String name;
    String created_at;
    String numOfSongs;
    String description;
    String member;

    public AlbumItem (String id, String name, String created_at, String numOfSongs, String description, String member) {
        this.id = id;
        this.name = name;
        this.created_at = created_at;
        this.numOfSongs = numOfSongs;
        this.description = description;
        this.member = member;

        Log.v("AlbumItem 객체 생성자", "생성자 종료됨");
    }

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getCreated_at(){
        return created_at;
    }
    public String getNumOfSongs(){
        return numOfSongs;
    }
    public String getDescription(){
        return description;
    }
    public String getMember(){
        return member;
    }
}