package com.example.letmesing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    final String TAG = this.getClass().getSimpleName();

    LinearLayout home_ly;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Log");

        init(); //객체 정의
        SettingListener(); //리스너 등록
        bottomNavigationView.setSelectedItemId(R.id.tab_home); //맨 처음 시작할 탭 설정

/*
//      연습 코드
        btn_2List = findViewById(R.id.button2);
        btn_2Map = findViewById(R.id.button3);
        btn_ect = findViewById(R.id.button4);
        btn_exit = findViewById(R.id.button5);
//        익명 class 사용 > 외부 변수 참조시 final 키워드 필요
        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.button2:
                        break;
                    case R.id.button3:
                        Intent myIntent = new Intent(MainActivity.this, MapActivity.class);
                        startActivity(myIntent);
//                        finish();
                        break;
                    case R.id.button4:
                        break;
                    case R.id.button5:
                        finish();
                        break;
                }
            }
        };
        btn_2List.setOnClickListener(click);
        btn_2Map.setOnClickListener(click);
        btn_ect.setOnClickListener(click);
        btn_exit.setOnClickListener(click);*/
    }
    private void init() {
        home_ly = findViewById(R.id.home_ly);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void SettingListener() {
        //선택 리스너 등록
        bottomNavigationView.setOnItemSelectedListener(new TabSelectedListener());
    }
    public class TabSelectedListener implements NavigationBarView.OnItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.tab_home: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new HomeFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_list: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new MyListFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_user: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new UserFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_map: {
                    Intent myIntent = new Intent(MainActivity.this, MapActivity.class);
                    startActivity(myIntent);
//                    finish();
                    return true;
                }
            }
            return false;
        }
    }
}