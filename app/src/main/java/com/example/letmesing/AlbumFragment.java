package com.example.letmesing;

import android.app.Activity;
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
    private UserInfo userinfo;
    private Button btn_addAlbum;
    private List<AlbumItem> albumList;
    private ListView lv_album;
    private static AlbumAdapter albumAdapter;
    private Activity parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_album, container, false);

        userinfo = ((MainActivity)getActivity()).getUserinfo(); // MainAcitivty 에 접근해서 userinfo 가져오기
        albumList = new ArrayList<AlbumItem>();
        albumList = get_album(userinfo.getId());


        lv_album = (ListView) rootView.findViewById(R.id.listview_album); // list view 연결
        albumAdapter = new AlbumAdapter(getContext(), albumList, this); // 생성한 data 로 adapter 생성
        lv_album.setAdapter(albumAdapter); // adapter 연결

        btn_addAlbum = (Button) rootView.findViewById(R.id.button_addAlbum);
        btn_addAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "Add New Album Clicked", Toast.LENGTH_SHORT).show();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                AddAlbumFragment addalbumFragment = new AddAlbumFragment(userinfo.getId());
                transaction.replace(R.id.layout_main, addalbumFragment);
                transaction.addToBackStack(null).commit();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private List<AlbumItem> get_album(String id) {
        List<AlbumItem> result = new ArrayList<AlbumItem>();
        Call<List<AlbumDM>> callSync = RetrofitClient.getApiService().album_api_get();  // album_single 로 받아와야함!!!! userinfo.getID 사용
        // api 의 동기적 처리를 위한 임시 thread 생성    Main thread 내에서는 네트워크 통신이 막혀있음 (thread 없이 단순 try-catch 로는 네트워크 통신 사용 불가)
        Thread th_temp = new Thread() {
            public void run() {
                Response<List<AlbumDM>> response;
                {
                    try {
                        response = callSync.execute();
                        List<AlbumDM> apiResponse = response.body();
                        // for (A:B) >> B 가 empty 할 때 까지 B 에서 차례대로 객체를 꺼내 A 에 넣겠다
                        for (AlbumDM album:apiResponse) {
                            if (album.getMember().compareTo(id)==0) {
                                AlbumItem temp = new AlbumItem(album.getId(), album.getName(), album.getCreated_at(), album.getNumOfSongs(), album.getDescription(), album.getMember());
                                result.add(temp);
                            }
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
        return result;
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