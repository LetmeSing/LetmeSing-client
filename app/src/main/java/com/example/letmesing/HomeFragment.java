package com.example.letmesing;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    retrofit2.Call<AlbumDM> call;
    private TextView tv_retrofit;
    private TextView tv_retrofit2;
    private ImageView iv_arrow1;
    private ImageView iv_arrow2;
    private ImageView iv_arrow3;
    private LinearLayout ll_karaoke;
    private LinearLayout ll_albumlist;
    private List<Recommend_DataDM> recommend_list;
    private Recommend_DataDM recommend_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        // 밑에 2개 버튼 화살표 색상 변경
//        iv_arrow1 = (ImageView) rootView.findViewById(R.id.imageView_arrow1);
        iv_arrow2 = (ImageView) rootView.findViewById(R.id.imageView_arrow2);
        iv_arrow3 = (ImageView) rootView.findViewById(R.id.imageView_arrow3);
//        iv_arrow1.setColorFilter(Color.parseColor("#FF4F5458"));
        iv_arrow2.setColorFilter(Color.parseColor("#FF4F5458"));
        iv_arrow3.setColorFilter(Color.parseColor("#FF4F5458"));

        ll_karaoke = (LinearLayout) rootView.findViewById(R.id.linearLayout_karaoke);
        ll_albumlist = (LinearLayout) rootView.findViewById(R.id.linearLayout_albumList);

        TextView tv_track = (TextView) rootView.findViewById(R.id.textView_recommend_track);
        TextView tv_artist = (TextView) rootView.findViewById(R.id.textView_recommend_artist);
        ImageView iv_image = (ImageView) rootView.findViewById(R.id.imageView_recommend_image);

        recommend_list = get_recommend();
        if (!recommend_list.isEmpty()) {
            for (int i=0; i<recommend_list.size(); i++) {
                Recommend_DataDM r = recommend_list.get(i);
                Log.d("출력: Rec " + i,  r.getArtist() + "\n" + r.getTrack() + "\n" + r.getImage());
            }
        }
        recommend_data = recommend_list.get(0);
        tv_artist.setText(recommend_data.getArtist());
        tv_track.setText(recommend_data.getTrack());
        String url = recommend_data.getImage();
        Glide.with(getActivity()).load(url).placeholder(R.drawable.place_holder).error(R.drawable.trash_bin).into(iv_image);

        ll_karaoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MapActivity.class);
                startActivity(intent);
            }
        });
        ll_albumlist.setOnClickListener(new View.OnClickListener() {
            FragmentTransaction transaction;
            @Override
            public void onClick(View view) {
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                AlbumFragment albumFragment = new AlbumFragment();
                transaction.replace(R.id.layout_main, albumFragment);
                transaction.addToBackStack(null).commit();
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.linearLayout_karaoke:
                        break;
                    case R.id.linearLayout_albumList:
                        break;
                }
            }
        };

        // Inflate the layout for this fragment
        return rootView;
    }
    private List<Recommend_DataDM> get_recommend () {
        final List<Recommend_DataDM>[] result = new ArrayList[]{new ArrayList<>()};

        Call<RecommendDM> callSync = RetrofitClient.getApiService().recommend_api_get();
        Thread th_temp = new Thread() {
            @Override
            public void run() {
                Response<RecommendDM> response;
                try {
                    response = callSync.execute();
                    RecommendDM apiResponse = response.body();
                    if (!response.isSuccessful()) {
                        Log.d("연결 비정상: Recommend", Integer.toString(response.code()));
                        return;
                    }
                    Log.d("연결 성공: Recommend ", Integer.toString(response.code()));
                    result[0] = apiResponse.getData();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        th_temp.start();
        try {
            th_temp.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result[0];
    }
}