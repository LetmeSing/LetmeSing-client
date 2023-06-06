package com.example.letmesing;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAlbumFragment extends Fragment {
    private Button btn_post_album;
    private EditText edtv_ablum_name;
    private EditText edtv_album_description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_album, container, false);

        btn_post_album = (Button) rootView.findViewById(R.id.button_postAlbum);
        btn_post_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtv_ablum_name = (EditText) rootView.findViewById(R.id.editText_album_name);
                edtv_album_description = (EditText) rootView.findViewById(R.id.editText_album_description);
                edtv_album_description = (EditText) rootView.findViewById(R.id.editText_album_description);
                String name = edtv_ablum_name.getText().toString();
                String description = edtv_album_description.getText().toString();
                // yyyy-mm-dd 형태로 오늘 날짜 받아오기
                Date nowDate = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String strNowDate = simpleDateFormat.format(nowDate);
                // Post
                AlbumDM albumDM = new AlbumDM(name, strNowDate,"0",description, "1");
                post_album(albumDM);
                // 현재 AddAlbumFragment 종료
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().remove(AddAlbumFragment.this).commit();
                manager.popBackStack();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }
    private void post_album (AlbumDM albumDM) {
        // API POST > 생성된 albumDM 객체 DB 에 저장
        Call<AlbumDM> callSync = RetrofitClient.getApiService().album_api_post(albumDM);
        Thread th_temp = new Thread() {
            public void run() {
                Response<AlbumDM> response;
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
}