package com.example.letmesing;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    retrofit2.Call<data_model> call;
    TextView tv_retrofit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        // retrofit 코드 시작
        tv_retrofit = (TextView) rootView.findViewById(R.id.textView_retrofit);
        call = retrofit_client.getApiService().test_api_get("1");
        call.enqueue(new Callback<data_model>(){
            //콜백 받는 부분
            @Override
            public void onResponse(retrofit2.Call<data_model> call, Response<data_model> response) {
                data_model result = response.body();
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
            public void onFailure(retrofit2.Call<data_model> call, Throwable t) {
            }
        }); // retrofit 코드 종료

        // Inflate the layout for this fragment
        return rootView;
    }
}