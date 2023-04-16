package com.example.letmesing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//        액션바 뒤로가기 (manifest 파일에서 parent 설정)
        getSupportActionBar().setTitle("Map activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}