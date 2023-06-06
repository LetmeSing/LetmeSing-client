package com.example.letmesing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterActivity extends AppCompatActivity {
    private UserInfo userinfo;
    private KaraokeDM karaoke;
    private TextView tv_master_name;
    private TextView tv_master_karaoke_name;
    private TextView tv_master_remainSeats;
    private TextView tv_master_totalSeats;
    private NumberPicker numberPicker;
    private Button btn_master_patch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        // Userinfo 객체 전달 받음
        Intent myintent = getIntent();
        if (myintent != null) {
            userinfo = (UserInfo) myintent.getSerializableExtra("user info");
        }
;
        tv_master_name = findViewById(R.id.textView_master_name);
        tv_master_karaoke_name = findViewById(R.id.textView_master_karaoke_name);
        tv_master_remainSeats = findViewById(R.id.textView_master_remainSeats);
        tv_master_totalSeats = findViewById(R.id.textView_master_totalSeats);

        karaoke = get_karaoke();
        tv_master_name.setText(userinfo.getNickname());
        tv_master_karaoke_name.setText(karaoke.getName());

        tv_master_remainSeats.setText(Integer.toString(karaoke.getRemainingSeat()));
        tv_master_totalSeats.setText(Integer.toString(karaoke.getTotalSeat()));

        
        // NumberPicker 구현
        numberPicker = findViewById(R.id.numberPicker_master_seats);
        numberPicker.setMaxValue(karaoke.getTotalSeat());
        numberPicker.setMinValue(0);
        numberPicker.setValue(karaoke.getRemainingSeat());

        // 참고 블로그
        // https://prince-mint.tistory.com/14
        numberPicker.setOnLongPressUpdateInterval(100); //길게 눌렀을 때 몇 초부터 반응?
        numberPicker.setWrapSelectorWheel(true); //최대값 or 최소값에서 멈출지 넘어갈지
        /*numberPicker.setOnScrollListener(new NumberPicker.OnScrollListener(){
            @Override
            public void onScrollStateChange(NumberPicker numberPicker, int state) {
                switch (state) {
                    case SCROLL_STATE_FLING :   // 손가락 튕길 때
                        Toast.makeText(getApplicationContext(), "플링", Toast.LENGTH_SHORT).show();
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL: // 손가락 떼지 않고 스크롤 중
//                        Toast.makeText(getApplicationContext(), "스크롤중", Toast.LENGTH_SHORT).show();
                        break;
                    case SCROLL_STATE_IDLE: // 정지 상태
                        break;
                }
            }
        });*/
        /*numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
//                Toast.makeText(getApplicationContext(), "" + oldVal +"에서"+ newVal +"(으)로", Toast.LENGTH_SHORT).show();
            }
        });*/


        btn_master_patch = findViewById(R.id.button_master_patch);
        btn_master_patch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patch_karaoke();
                Intent intent = getIntent();
                startActivity(intent); //액티비티 열기
                overridePendingTransition(0, 0);//인텐트 효과 없애기
                finish();//인텐트 종료
            }
        });
    }

    private void patch_karaoke () {
        Call<KaraokeDM> callSync = RetrofitClient.getApiService().karaoke_api_patch(karaoke.getId(), numberPicker.getValue());
        Thread th_temp = new Thread() {
            public void run() {
                Response<KaraokeDM> response;
                try {
                    response = callSync.execute();
                    if (response.isSuccessful()) {
                        KaraokeDM apiResponse = response.body();
                        Log.d("Patch 성공", karaoke.getName() + " " + karaoke.getRemainingSeat());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        th_temp.start();
        try {
            th_temp.join(); // api 를 통해 data 를 받기전에 UI 가 먼저 생성되는 경우 막기 위한 join
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Toast.makeText(getApplicationContext(), "여석이 변경되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private KaraokeDM get_karaoke () {
        KaraokeDM result = null;
        ArrayList<KaraokeDM> karaokeList = new ArrayList<KaraokeDM>();
        Call<List<KaraokeDM>> callSync = RetrofitClient.getApiService().karaoke_api_get();
        // api 의 동기적 처리를 위한 임시 thread 생성    Main thread 내에서는 네트워크 통신이 막혀있음 (thread 없이 단순 try-catch 로는 네트워크 통신 사용 불가)
        Thread th_temp = new Thread() {
            public void run() {
                Response<List<KaraokeDM>> response;
                try {
                    response = callSync.execute();
                    List<KaraokeDM> apiResponse = response.body();
                    // for (A:B) >> B 가 empty 할 때 까지 B 에서 차례대로 객체를 꺼내 A 에 넣겠다
                    for (KaraokeDM karaoke:apiResponse) {
                        karaokeList.add(karaoke);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        th_temp.start();
        try {
            th_temp.join(); // api 를 통해 data 를 받기전에 UI 가 먼저 생성되는 경우 막기 위한 join
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (int i=0; i<karaokeList.size(); i++) {
            if (karaokeList.get(i).getMemberId().compareTo(userinfo.getId()) == 0) {
                result = karaokeList.get(i);
            }
        }
        return result;
    }
}