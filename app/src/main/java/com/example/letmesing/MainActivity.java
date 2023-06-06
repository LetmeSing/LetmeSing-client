package com.example.letmesing;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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

        init(); //객체 정의
        SettingListener(); //리스너 등록
        bottomNavigationView.setSelectedItemId(R.id.tab_home); //맨 처음 시작할 탭 설정
    }
    private void init() {
        ly_main = findViewById(R.id.layout_main);
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
                    // bottomNavi 로 home 화면 복귀시 back stack 초기화
                    FragmentManager manager = getSupportFragmentManager();
                    for(int i = 0; i < manager.getBackStackEntryCount(); ++i) {
                        manager.popBackStack();
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.layout_main, new HomeFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_list: {
                    Bundle mybundle = new Bundle();   // 번들 생성
                    mybundle.putSerializable("user info", userinfo);  // 데이터 적재
                    AlbumFragment albumFragment = new AlbumFragment();
                    albumFragment.setArguments(mybundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.layout_main, albumFragment)
                            .commit();
                    return true;
                }
                case R.id.tab_user: {
                    Bundle mybundle = new Bundle();   // 번들 생성
                    mybundle.putSerializable("user info", userinfo);  // 데이터 적재
                    SettingFragment settingFragment = new SettingFragment();    // 프래그먼트 선언
                    settingFragment.setArguments(mybundle);   // 프래그먼트에 데이터 전달
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