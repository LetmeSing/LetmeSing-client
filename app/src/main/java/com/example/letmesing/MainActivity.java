package com.example.letmesing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    final String TAG = this.getClass().getSimpleName();
    private UserInfo userinfo;    // 현재 userinfo 는 수정할 방법 없음

    FrameLayout ly_main;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Userinfo 객체 전달 받음
        Intent myintent = getIntent();
        if (myintent != null) {
            userinfo = (UserInfo) myintent.getSerializableExtra("user info");
        }

        ly_main = findViewById(R.id.layout_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new TabSelectedListener());  //선택 리스너 등록
        bottomNavigationView.setSelectedItemId(R.id.tab_home); //맨 처음 시작할 탭 설정
    }

    public UserInfo getUserinfo () {
        return this.userinfo;
    }
    public class TabSelectedListener implements NavigationBarView.OnItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.tab_home: {
                    // bottomNavi 로 home 화면 복귀시 back stack 초기화
                    FragmentManager manager = getSupportFragmentManager();
                    for(int i = 0; i < manager.getBackStackEntryCount(); ++i) {
                        manager.popBackStack();
                    }
                    manager.beginTransaction()
                            .replace(R.id.layout_main, new HomeFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_list: {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction().replace(R.id.layout_main, new AlbumFragment());
                    transaction.addToBackStack(null);   // bottomNavi 로 앨범화면에 접근해도 버튼으로 접근한것과 같게 backstack 에 추가
                    transaction.commit();
                    return true;
                }
                case R.id.tab_user: {
                    Bundle mybundle2 = new Bundle();   // 번들 생성
                    mybundle2.putSerializable("user info", userinfo);  // 데이터 적재
                    SettingFragment settingFragment = new SettingFragment();    // 프래그먼트 선언
                    settingFragment.setArguments(mybundle2);   // 프래그먼트에 데이터 전달
                    getSupportFragmentManager().beginTransaction()  // 프래그먼트 화면 띄우기
                            .replace(R.id.layout_main, settingFragment)
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