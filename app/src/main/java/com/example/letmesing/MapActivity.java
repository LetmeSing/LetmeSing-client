package com.example.letmesing;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private List<TempPlace> placeList;
    private RecyclerView recyclerView;
    private PlaceAdapter placeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // 지도 fragment를 가져와서 비동기적으로 맵을 준비합니다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        // 장소 목록 초기화
        placeList = new ArrayList<>();

        // 더미 데이터
        LatLng latLng1 = new LatLng(37.507063, 126.958612);
        TempPlace place1 = new TempPlace("슈퍼스타코인노래연습장", "서울특별시 동작구 흑석동 195-30번지", 37.507063, 126.958612, 18);
        placeList.add(place1);

        LatLng latLng2 = new LatLng(37.507340, 126.959159);
        TempPlace place2 = new TempPlace("비트코인동전노래방", "서울특별시 동작구 흑석동 번지 지층 190-33", 37.507340, 126.959159, 15);
        placeList.add(place2);

        LatLng latLng3 = new LatLng(37.507021, 126.958560);
        TempPlace place3 = new TempPlace("잇츠코인노래방", "서울특별시 동작구 흑석동 195-17번지 3층", 37.507021, 126.958560, 20);
        placeList.add(place3);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 지도 초기 위치 설정
        LatLng karaoke0 = new LatLng(37.507063, 126.958612);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(karaoke0, 18));

        // 마커 생성 및 추가
        for (TempPlace tempPlace : placeList) {
            double latitude = tempPlace.getLatitude();
            double longitude = tempPlace.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(tempPlace.getName())
                    .snippet(tempPlace.getAddress());
            mMap.addMarker(markerOptions).setTag(tempPlace);
        }

        // 정보창 인터페이스
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(@NonNull Marker marker) {
                return null; // 기본 정보창 사용
            }

            @Override
            public View getInfoContents(@NonNull Marker marker) {
                // 커스텀 정보창을 위한 View 객체 생성
                @SuppressLint("InflateParams") View infoWindowView = getLayoutInflater().inflate(R.layout.custom_info_window, null);

                // View 내부의 요소들을 찾아옴
                TextView titleTextView = infoWindowView.findViewById(R.id.titleTextView);
                TextView snippetTextView = infoWindowView.findViewById(R.id.snippetTextView);
                TextView remainingSeatTextView = infoWindowView.findViewById(R.id.remainingSeatView);

                // 마커에 설정된 태그를 가져옴
                Object tag = marker.getTag();

                if (tag instanceof TempPlace) {
                    // 마커의 태그로 설정된 TempPlace 객체를 가져옴
                    TempPlace tempPlace = (TempPlace) tag;

                    // TempPlace 객체에서 이름, 주소, 잔여 좌석 수를 가져와서 View에 표시
                    titleTextView.setText(tempPlace.getName());
                    snippetTextView.setText(tempPlace.getAddress());
                    remainingSeatTextView.setText(String.valueOf(tempPlace.getRemainingSeat()));
                }

                return infoWindowView;
            }
        });

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PlaceAdapter placeAdapter = new PlaceAdapter(placeList);
        recyclerView.setAdapter(placeAdapter);

        // 마커 클릭 이벤트 처리
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        // 마커를 클릭했을 때 팝업 정보 띄우기
        marker.showInfoWindow();
        return true;
    }
}