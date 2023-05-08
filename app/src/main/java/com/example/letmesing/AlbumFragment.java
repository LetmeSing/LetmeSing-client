package com.example.letmesing;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

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
        for (int i=1; i<6; i++) {
            albumList.add(new AlbumItem(Integer.toString(i), "Album "+i, "2023-05-0"+i, "5", "description\n~2nd line"+i, "1"));
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