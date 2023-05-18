package com.example.letmesing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final long MIN_TIME_BETWEEN_UPDATES = 1000; // 1초
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10.0f; // 10미터

    private GoogleMap mMap;
    private List<TempPlace> placeList;
    private PlaceAdapter placeAdapter;
    // Retrofit 객체와 API 서비스 선언
    private Retrofit_interface retrofitService;

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

        // API 요청 보내기
        getPlaceListFromServer();

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


    /*        // 더미 데이터
            LatLng latLng1 = new LatLng(37.507063, 126.958612);
            TempPlace place1 = new TempPlace("슈퍼스타코인노래연습장", "서울특별시 동작구 흑석동 195-30번지", 37.507063, 126.958612, 18);
            placeList.add(place1);

            LatLng latLng2 = new LatLng(37.507340, 126.959159);
            TempPlace place2 = new TempPlace("비트코인동전노래방", "서울특별시 동작구 흑석동 번지 지층 190-33", 37.507340, 126.959159, 15);
            placeList.add(place2);

            LatLng latLng3 = new LatLng(37.507021, 126.958560);
            TempPlace place3 = new TempPlace("잇츠코인노래방", "서울특별시 동작구 흑석동 195-17번지 3층", 37.507021, 126.958560, 20);
            placeList.add(place3);
    */
    private void getPlaceListFromServer() {
        Call<List<TempPlace>> call = retrofitService.seat_api_get();
        call.enqueue(new Callback<List<TempPlace>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<TempPlace>> call, @NonNull Response<List<TempPlace>> response) {
                if (response.isSuccessful()) {
                    // API 응답이 성공적으로 도착한 경우 장소 목록을 가져와서 처리
                    placeList = response.body();
                    placeAdapter.setPlaceList(placeList); // 어댑터에 장소 목록 설정
                    placeAdapter.notifyDataSetChanged(); // RecyclerView 어댑터에 변경 내용을 알려줌

                    // 맵이 준비되었을 때 마커 추가
                    if (mMap != null) {
                        addMarkersToMap();
                    }
                } else {
                    // API 요청이 실패한 경우에 대한 처리
                    Toast.makeText(MapActivity.this, "Failed to fetch place data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<TempPlace>> call, @NonNull Throwable t) {
                // API 요청 실패에 대한 처리
                Toast.makeText(MapActivity.this, "Failed to connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 현재 위치를 가져오는 메서드
    private LatLng getCurrentLocation() {
        // 위치 관리자 객체 생성
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 위치 권한이 허용되어 있는지 확인
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // 위치 권한이 허용되어 있다면 현재 위치를 가져옴
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                // 현재 위치의 위도와 경도를 LatLng 객체로 변환하여 반환
                return new LatLng(location.getLatitude(), location.getLongitude());
            }
        }

        return null; // 위치를 가져오지 못한 경우 null 반환
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        MapStyleOptions styleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style);
        googleMap.setMapStyle(styleOptions);
        // 기본 마커 비활성화
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));

        // 기본 마커 비활성화
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(false); // 기본 마커 비활성화

        // 현재 위치를 가져옴
        LatLng currentLocation = getCurrentLocation();

        // 현재 위치가 유효한 경우에만 카메라 이동
        if (currentLocation != null) {
            // 카메라를 현재 위치로 이동
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18));
            Toast.makeText(MapActivity.this, "카메라를 현재 위치로 이동했습니다.", Toast.LENGTH_SHORT).show();
        }
        else {
            // 지도 초기 위치 설정
            LatLng karaoke0 = new LatLng(37.507075, 126.958615);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(karaoke0, 18));
            Toast.makeText(MapActivity.this, "카메라를 초기 위치로 이동했습니다.", Toast.LENGTH_SHORT).show();
        }
        // 서버에서 받은 장소 목록으로 마커 추가
        addMarkersToMap();

        // 정보창 인터페이스
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(@NonNull Marker marker) {
                return null; // 기본 정보창 사용
            }

            @SuppressLint("SetTextI18n")
            @Override
            public View getInfoContents(@NonNull Marker marker) {
                // 커스텀 정보창을 위한 View 객체 생성
                @SuppressLint("InflateParams") View infoWindowView = getLayoutInflater().inflate(R.layout.custom_info_window, null);

                // View 내부의 요소들을 찾아옴
                TextView titleTextView = infoWindowView.findViewById(R.id.titleTextView);
                //TextView snippetTextView = infoWindowView.findViewById(R.id.snippetTextView);
                TextView remainingSeatTextView = infoWindowView.findViewById(R.id.remainingSeatView);

                // 마커에 설정된 태그를 가져옴
                Object tag = marker.getTag();

                if (tag instanceof TempPlace) {
                    // 마커의 태그로 설정된 TempPlace 객체를 가져옴
                    TempPlace tempPlace = (TempPlace) tag;

                    // TempPlace 객체에서 이름, 주소, 잔여 좌석 수를 가져와서 View에 표시
                    titleTextView.setText(tempPlace.getName());
                    //snippetTextView.setText(tempPlace.getAddress());
                    // "여석: " 텍스트 추가
                    remainingSeatTextView.setText("여석: " + tempPlace.getRemainingSeat());
                }

                return infoWindowView;
            }
        });

        // RecyclerView 초기화
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        placeAdapter = new PlaceAdapter(placeList); // 객체 생성과 초기화
        recyclerView.setAdapter(placeAdapter);

        // placeAdapter 객체가 생성된 후에 setOnItemClickListener() 호출
        placeAdapter.setOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TempPlace place) {
                // 선택한 장소의 마커를 가져옴
                Marker marker = getMarkerForPlace(position);
                if (marker != null) {
                    // 마커를 클릭한 것처럼 처리하여 InfoWindow를 표시
                    marker.showInfoWindow();

                    // 마커가 가운데로 오도록 카메라 이동
                    LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }
        });

        // 마커 클릭 이벤트 처리
        mMap.setOnMarkerClickListener(this);
    }

    private void addMarkerToMap(TempPlace place) {
        double latitude = place.getLatitude();
        double longitude = place.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(place.getName())
                .snippet(place.getAddress());
        Marker marker = mMap.addMarker(markerOptions);
        assert marker != null;
        marker.setTag(place);
        markerMap.put(place.getId(), marker);
    }

    private void addMarkersToMap() {
        mMap.clear();
        for (TempPlace place : placeList) {
            addMarkerToMap(place);
        }
    }
    @Override
    public boolean onMarkerClick(final Marker marker) {
        // 마커를 클릭했을 때 팝업 정보 띄우기
        marker.showInfoWindow();
        return true;
    }


}