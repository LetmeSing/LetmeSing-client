package com.example.letmesing;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    retrofit2.Call<DataModel> call;
    TextView tv_retrofit;
    TextView tv_retrofit2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

/*

        // retrofit 코드 시작
        tv_retrofit = (TextView) rootView.findViewById(R.id.textView_retrofit);
//        call = RetrofitClient.getApiService().test_api_get("1");
        call = RetrofitClient.getApiService().test_api_get("1");

        call.enqueue(new Callback<DataModel>(){
            //콜백 받는 부분
            @Override
            public void onResponse(retrofit2.Call<DataModel> call, Response<DataModel> response) {
                DataModel result = response.body();
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
            public void onFailure(retrofit2.Call<DataModel> call, Throwable t) {
                tv_retrofit.setText("onFailure[1]: \n"+t.getMessage());
            }
        }); // retrofit 코드 종료

        // 전체 data 받아오는 my_api_get() 테스트용 call2
        // my_api_get() 은 특정 usr_id 가 아니라 전체 data 를 list<> 형태로 받아옴
        tv_retrofit2 = (TextView) rootView.findViewById(R.id.textView_retrofit2);
        Call<List<DataModel>> call2 = RetrofitClient.getApiService().my_api_get();
        call2.enqueue(new Callback<List<DataModel>>() {
            @Override
            public void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> response) {
                if (!response.isSuccessful()) {
                    tv_retrofit2.setText("Error Code: " + response.code());
                    return;
                }
                List<DataModel> albums = response.body();

                // for (A:B) >> B 가 empty 할 때 까지 B 에서 차례대로 객체를 꺼내 A 에 넣겠다
                for (DataModel album:albums) {
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
            public void onFailure(Call<List<DataModel>> call, Throwable t) {
                tv_retrofit2.setText("onFailure[2]: \n"+t.getMessage());
            }
        });
*/

        // Inflate the layout for this fragment
        return rootView;
    }
}