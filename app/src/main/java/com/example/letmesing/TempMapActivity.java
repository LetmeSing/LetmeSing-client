package com.example.letmesing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import retrofit2.Retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class TempMapActivity extends AppCompatActivity {

    // 서버로부터 받아온 JSON을 저장할 변수
    private String jsonString;

    // JSON을 표시할 텍스트 뷰
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_map);

        textView = findViewById(R.id.textView);  // 텍스트 뷰 초기화

        fetchJsonFromServer();  // JSON 데이터를 서버에서 가져오는 메서드 호출
    }

    // 서버로부터 JSON을 받아오는 메서드
    private void fetchJsonFromServer() {
        // Retrofit 인스턴스 생성
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://letmesing.kro.kr:8000/")  // 서버의 기본 URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Retrofit 인터페이스를 사용하여 API 호출
        Retrofit_interface apiService = retrofit.create(Retrofit_interface.class);
        Call<List<Place>> call = apiService.seat_api_get();

        // 비동기적으로 API 호출 실행
        call.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                if (response.isSuccessful()) {
                    List<Place> placeList = response.body();  // 서버로부터 받은 Place 객체 리스트
                    if (placeList != null && placeList.size() > 0) {
                        StringBuilder builder = new StringBuilder();
                        for (Place place : placeList) {
                            builder.append("Name: ").append(place.getName()).append("\n")
                                    .append("Address: ").append(place.getAddress()).append("\n")
                                    .append("Latitude: ").append(place.getLatitude()).append("\n")
                                    .append("Longitude: ").append(place.getLongitude()).append("\n")
                                    .append("Remaining Seat: ").append(place.getRemainingSeat()).append("\n")
                                    .append("Total Seat: ").append(place.getTotalSeat()).append("\n")
                                    .append("\n");
                        }
                        jsonString = builder.toString();
                        showJsonOnTextView();  // 텍스트 뷰에 JSON 표시
                    } else {
                        textView.setText("데이터가 없습니다.");
                    }
                } else {
                    // API 호출 실패 시 에러 처리
                    textView.setText("API 호출 실패");
                }
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                // API 호출 실패 시 에러 처리
                textView.setText("API 호출 실패: " + t.getMessage());
            }
        });
    }

    // 텍스트 뷰에 JSON을 표시하는 메서드
    private void showJsonOnTextView() {
        // 텍스트 뷰에 JSON 문자열 설정
        textView.setText(jsonString);
    }
}
