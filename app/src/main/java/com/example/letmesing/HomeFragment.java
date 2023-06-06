package com.example.letmesing;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        // 밑에 2개 버튼 화살표 색상 변경
        iv_arrow1 = (ImageView) rootView.findViewById(R.id.imageView_arrow1);
        iv_arrow2 = (ImageView) rootView.findViewById(R.id.imageView_arrow2);
        iv_arrow3 = (ImageView) rootView.findViewById(R.id.imageView_arrow3);
        iv_arrow1.setColorFilter(Color.parseColor("#FF4F5458"));
        iv_arrow2.setColorFilter(Color.parseColor("#FF4F5458"));
        iv_arrow3.setColorFilter(Color.parseColor("#FF4F5458"));

        ll_karaoke = (LinearLayout) rootView.findViewById(R.id.linearLayout_karaoke);
        ll_albumlist = (LinearLayout) rootView.findViewById(R.id.linearLayout_albumList);

        ll_karaoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MapActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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

/*

        // retrofit 코드 시작
        tv_retrofit = (TextView) rootView.findViewById(R.id.textView_retrofit);
//        call = RetrofitClient.getApiService().test_api_get("1");
        call = RetrofitClient.getApiService().test_api_get("1");

        call.enqueue(new Callback<AlbumDM>(){
            //콜백 받는 부분
            @Override
            public void onResponse(retrofit2.Call<AlbumDM> call, Response<AlbumDM> response) {
                AlbumDM result = response.body();
                String str;
                str= result.getId() +"\n"+
                        result.getName()+"\n"+
                        result.getCreated_at()+"\n"+
                        result.getNumOfSongs()+"\n"+
                        result.getDescription()+"\n"+
                        result.getMember();
                tv_retrofit.setText(str);
            }

            @Override
            public void onFailure(retrofit2.Call<AlbumDM> call, Throwable t) {
                tv_retrofit.setText("onFailure[1]: \n"+t.getMessage());
            }
        }); // retrofit 코드 종료

        // 전체 data 받아오는 my_api_get() 테스트용 call2
        // my_api_get() 은 특정 usr_id 가 아니라 전체 data 를 list<> 형태로 받아옴
        tv_retrofit2 = (TextView) rootView.findViewById(R.id.textView_retrofit2);
        Call<List<AlbumDM>> call2 = RetrofitClient.getApiService().my_api_get();
        call2.enqueue(new Callback<List<AlbumDM>>() {
            @Override
            public void onResponse(Call<List<AlbumDM>> call, Response<List<AlbumDM>> response) {
                if (!response.isSuccessful()) {
                    tv_retrofit2.setText("Error Code: " + response.code());
                    return;
                }
                List<AlbumDM> albums = response.body();

                // for (A:B) >> B 가 empty 할 때 까지 B 에서 차례대로 객체를 꺼내 A 에 넣겠다
                for (AlbumDM album:albums) {
                    String content = "";
                    content += album.getId() +"\n"+
                            album.getName()+"\n"+
                            album.getCreated_at()+"\n"+
                            album.getNumOfSongs()+"\n"+
                            album.getDescription()+"\n"+
                            album.getMember() + "\n\n";
                    tv_retrofit2.append(content);
                }
            }
            @Override
            public void onFailure(Call<List<AlbumDM>> call, Throwable t) {
                tv_retrofit2.setText("onFailure[2]: \n"+t.getMessage());
            }
        });
*/

        // Inflate the layout for this fragment
        return rootView;
    }
}