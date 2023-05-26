package com.example.letmesing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class MasterActivity extends AppCompatActivity {
    private UserInfo userinfo;
    private TextView tv_master_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        // Userinfo 객체 전달 받음
        Intent myintent = getIntent();
        if (myintent != null) {
            userinfo = (UserInfo) myintent.getSerializableExtra("user info");
        }

        tv_master_name = findViewById(R.id.textView_master_name);
        tv_master_name.setText(userinfo.getNickname());
        
        // NumberPicker 구현
        NumberPicker numberPicker = findViewById(R.id.numberPicker_master_seats);
        numberPicker.setMaxValue(100);
        numberPicker.setMinValue(0);
        numberPicker.setValue(7);   // 이거 GET 해서 값 얻어와야함 ㅅㅂㅋㅋㅋㅋㅋㅋㅋ 아 귀찮아

//        String [] test = {"test1","test2"};
//        numberPicker.setDisplayedValues(test);

        // 참고 블로그
        // https://prince-mint.tistory.com/14
        numberPicker.setOnLongPressUpdateInterval(100); //길게 눌렀을 때 몇 초부터 반응?
        numberPicker.setWrapSelectorWheel(true); //최대값 or 최소값에서 멈출지 넘어갈지
        numberPicker.setOnScrollListener(new NumberPicker.OnScrollListener(){
            @Override
            public void onScrollStateChange(NumberPicker numberPicker, int state) {
                switch (state) {
                    case SCROLL_STATE_FLING :   // 손가락 튕길 때
                        Toast.makeText(getApplicationContext(), "플링", Toast.LENGTH_SHORT).show();
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL: // 손가락 떼지 않고 스크롤 중
                        Toast.makeText(getApplicationContext(), "스크롤중", Toast.LENGTH_SHORT).show();
                        break;
                    case SCROLL_STATE_IDLE: // 정지 상태
                        break;
                }
            }
        });
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
//                Toast.makeText(getApplicationContext(), "" + oldVal +"에서"+ newVal +"(으)로", Toast.LENGTH_SHORT).show();
            }
        });
    }
}