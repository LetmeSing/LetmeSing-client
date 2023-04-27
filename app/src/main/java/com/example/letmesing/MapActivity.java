package com.example.letmesing;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // 지도 fragment를 가져와서 비동기적으로 맵을 준비합니다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 더미 데이터를 사용하여 마커를 추가합니다.
        LatLng karaoke1 = new LatLng(37.507063, 126.958612);
        mMap.addMarker(new MarkerOptions().position(karaoke1).title("슈퍼스타코인노래연습장").snippet("서울특별시 마포구 성산동 515-3"));


        LatLng karaoke2 = new LatLng(37.507340, 126.959159);
        mMap.addMarker(new MarkerOptions().position(karaoke2).title("비트코인동전노래방").snippet("서울특별시 동작구 흑석동 번지 지층 190-33"));

        LatLng karaoke3 = new LatLng(37.507021, 126.958560);
        mMap.addMarker(new MarkerOptions().position(karaoke3).title("잇츠코인노래방").snippet("서울특별시 동작구 흑석동 195-17번지 3층"));

        //지도초기 위치 설정
        LatLng karaoke0 = new LatLng(37.507063, 126.958612);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(karaoke0, 18));

        //마커 클릭 이벤트 처리
        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        // 마커를 클릭했을 때 팝업 정보 띄우기
        marker.showInfoWindow();
        return true;
    }
}