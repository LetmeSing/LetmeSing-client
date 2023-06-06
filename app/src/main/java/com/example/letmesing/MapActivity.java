package com.example.letmesing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, PlaceAdapter.OnItemClickListener {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final long MIN_TIME_BETWEEN_UPDATES = 1000; // 1초
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10.0f; // 10미터

    private GoogleMap mMap;
    private CustomInfoWindowAdapter infoWindowAdapter;
    private List<TempPlace> placeList;
    private PlaceAdapter placeAdapter;
    // Retrofit 객체와 API 서비스 선언
    private Retrofit_interface retrofitService;
    private Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Retrofit 인터페이스를 가져옴
        retrofitService = RetrofitClient.getApiService();

        // 지도 fragment를 가져와서 비동기적으로 맵을 준비합니다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        // 장소 목록 초기화
        placeList = new ArrayList<>();

        // RecyclerView 초기화
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        placeAdapter = new PlaceAdapter(placeList); // 객체 생성과 초기화
        placeAdapter.setOnItemClickListener(this); // 리스너 설정
        recyclerView.setAdapter(placeAdapter);

        // API 요청 보내기
        getPlaceListFromServer();

        refreshButton = findViewById(R.id.refreshButton);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 액티비티를 새로고침하는 동작 수행
                recreate(); // 현재 액티비티를 다시 생성하여 새로고침
            }
        });
    }

    @Override
    public void onItemClick(int position, TempPlace place) {
        // 클릭한 위치의 마커를 가져옴
        Marker marker = getMarkerForPlace(position);
        if (marker != null) {
            // 마커의 위치로 카메라 이동
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15);
            mMap.animateCamera(cameraUpdate);
            // 마커 클릭 이벤트 호출하여 InfoWindow 표시
            marker.showInfoWindow();
        }
    }

    private final Map<String, Marker> markerMap = new HashMap<>();

    private Marker getMarkerForPlace(int position) {
        TempPlace place = placeList.get(position);
        String placeId = place.getId();
        if (markerMap.containsKey(placeId)) {
            return markerMap.get(placeId);
        }
        return null;
    }

    private void getPlaceListFromServer() {
        Call<List<TempPlace>> call = retrofitService.seat_api_get();
        call.enqueue(new Callback<List<TempPlace>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<TempPlace>> call, @NonNull Response<List<TempPlace>> response) {
                if (response.isSuccessful()) {
                    // API 응답이 성공적으로 도착한 경우 장소 목록을 가져와서 처리
                    placeList = response.body();
                    placeAdapter.setPlaceList(placeList); // 어댑터에 목록 설정
                    placeAdapter.notifyDataSetChanged(); // 어댑터 갱신

                    // 기존의 마커들을 제거
                    for (Marker marker : markerMap.values()) {
                        marker.remove();
                    }
                    markerMap.clear();

                    // 새로운 목록의 마커들을 추가
                    for (int i = 0; i < placeList.size(); i++) {
                        TempPlace place = placeList.get(i);
                        LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(latLng)
                                .title(place.getName())
                                .snippet(place.getAddress());
                        if(place.getRemainingSeat()<6){markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.gray_light));}
                        else if(place.getRemainingSeat()<8){markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.gray));}
                        else {markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.gray_dark));}

                        Marker marker = mMap.addMarker(markerOptions);
                        marker.setTag(place); // 마커의 태그로 TempPlace 객체 설정
                        markerMap.put(place.getId(), marker);
                    }
                } else {
                    Toast.makeText(MapActivity.this, "API 요청에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<TempPlace>> call, @NonNull Throwable t) {
                Toast.makeText(MapActivity.this, "API 요청에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 맵 스타일 설정
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

        // 맵을 사용자 현재 위치로 이동
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            mMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BETWEEN_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            // 위치 변경 이벤트 처리
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                            // 위치 제공자 상태 변경 이벤트 처리
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            // 위치 제공자 활성화 이벤트 처리
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            // 위치 제공자 비활성화 이벤트 처리
                        }
                    });
        }

        // 마커 클릭 리스너 등록
        mMap.setOnMarkerClickListener(this);

        // InfoWindow 어댑터 설정
        infoWindowAdapter = new CustomInfoWindowAdapter(this);
        mMap.setInfoWindowAdapter(infoWindowAdapter);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // 마커 클릭 시 InfoWindow 표시
        marker.showInfoWindow();
        return true;
    }
}