package com.example.letmesing;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, PlaceAdapter.OnItemClickListener {
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private GoogleMap mMap;
    private List<TempPlace> placeList;
    private PlaceAdapter placeAdapter;
    // Retrofit 객체와 API 서비스 선언
    private Retrofit_interface retrofitService;
    private LatLng cameraPosition;
    private FusedLocationProviderClient fusedLocationClient;

    private LinearLayout recyclerViewContainer;

    private float downY;
    private int originalHeight;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // 액티비티나 프래그먼트에서 위치 권한을 요청하는 코드

        // 위치 권한 요청
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }

        // Retrofit 인터페이스를 가져옴
        retrofitService = RetrofitClient.getApiService();

        // 이전 카메라 좌표가 있는지 확인하고 복구
        if (savedInstanceState != null && savedInstanceState.containsKey("cameraPosition")) {
            cameraPosition = savedInstanceState.getParcelable("cameraPosition");
        }

        // 지도 초기화
        initializeMap();
        getPlaceListFromServer();

        // RecyclerView 초기화
        initializeRecyclerView();

        // 새로고침 버튼 설정
        setupRefreshButton();
    }

    private void initializeMap() {
        // 장소 목록 초기화
        placeList = new ArrayList<>();
        // 지도 fragment를 가져와서 비동기적으로 맵을 준비합니다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;

            // FusedLocationProviderClient 초기화
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(MapActivity.this);

            // 맵 스타일 설정
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapActivity.this, R.raw.map_style));

            // 마커 클릭 리스너 등록
            mMap.setOnMarkerClickListener(MapActivity.this);

            // InfoWindow 어댑터 설정
            CustomInfoWindowAdapter infoWindowAdapter = new CustomInfoWindowAdapter(MapActivity.this);
            mMap.setInfoWindowAdapter(infoWindowAdapter);

            // 위치 권한 체크 및 요청
            if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            } else {
                // 위치 권한이 있는 경우 현재 위치 가져오기
                getLastLocation();
            }
        });
    }
    // dp를 px로 변환하는 메서드
    private int convertDpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initializeRecyclerView() {
        //recyclerViewContainer = findViewById(R.id.recyclerViewContainer);
        // RecyclerView 초기화
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // RecyclerView의 LayoutParams 가져오기
        //ViewGroup.LayoutParams recyclerViewLayoutParams = recyclerView.getLayoutParams();

        // RecyclerViewContainer의 높이에서 50dp를 뺀 값으로 RecyclerView의 높이 설정
        //int newHeight = recyclerViewContainer.getHeight() - 50;
        //recyclerViewLayoutParams.height = newHeight;

        // 수정한 LayoutParams를 RecyclerView에 설정
        //recyclerView.setLayoutParams(recyclerViewLayoutParams);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        placeAdapter = new PlaceAdapter(placeList); // 객체 생성과 초기화
        placeAdapter.setOnItemClickListener(this); // 리스너 설정
        recyclerView.setAdapter(placeAdapter);

        // recyclerViewContainer의 원래 높이를 저장
        //originalHeight = recyclerViewContainer.getLayoutParams().height;

        /*recyclerViewContainer.setOnTouchListener((v, event) -> {
            handleSlidingTouchEvent(event);
            return true;
        });*/
    }
    /*
    private void handleSlidingTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float upY = event.getY();
                float deltaY = upY - downY;
                if (deltaY > 0 && isRecyclerViewVisible()) {
                    // 슬라이드 다운 처리
                    minimizeRecyclerView();
                } else if (deltaY < 0 && !isRecyclerViewVisible()) {
                    // 슬라이드 업 처리
                    expandRecyclerView();
                }
                break;
        }
    }
    private void minimizeRecyclerView() {
        ViewGroup.LayoutParams layoutParams = recyclerViewContainer.getLayoutParams();
        layoutParams.height = 50;
        recyclerViewContainer.setLayoutParams(layoutParams);
    }

    private void expandRecyclerView() {
        ViewGroup.LayoutParams layoutParams = recyclerViewContainer.getLayoutParams();
        layoutParams.height = originalHeight;
        recyclerViewContainer.setLayoutParams(layoutParams);
    }*/

    private void setupRefreshButton() {
        ImageButton refreshButton = findViewById(R.id.refreshButton);

        refreshButton.setOnClickListener(v -> {
            cameraPosition = mMap.getCameraPosition().target;
            // 액티비티를 재시작하는 동작 수행
            //Toast.makeText(MapActivity.this,"현재위치 저장",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
        });
    }
    /*
    private boolean isRecyclerViewVisible() {
        return recyclerViewContainer.getLayoutParams().height < originalHeight;
    }
*/
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // 현재 카메라 좌표를 저장
        if (mMap != null) {
            outState.putParcelable("cameraPosition", mMap.getCameraPosition().target);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null && cameraPosition != null) {
            // 이전 카메라 위치로 지도를 설정
            mMap.moveCamera(CameraUpdateFactory.newLatLng(cameraPosition));
            Toast.makeText(MapActivity.this, "기존 위치로 이동", Toast.LENGTH_SHORT).show();
            // 이후에 저장한 카메라 위치를 초기화
            cameraPosition = null;
        }
    }

    @Override
    public void onItemClick(int position, TempPlace place) {
        // 클릭한 위치의 마커를 가져옴
        Marker marker = getMarkerForPlace(position);
        if (marker != null) {
            // 마커의 위치로 카메라 이동
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 18);
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
                        if (place.getRemainingSeat() < 6) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.gray_light));
                        } else if (place.getRemainingSeat() < 8) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.gray));
                        } else {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.gray_dark));
                        }

                        Marker marker = mMap.addMarker(markerOptions);
                        assert marker != null;
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // 맵 스타일 설정
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

        // 마커 클릭 리스너 등록
        mMap.setOnMarkerClickListener(this);

        // InfoWindow 어댑터 설정
        CustomInfoWindowAdapter infoWindowAdapter = new CustomInfoWindowAdapter(this);
        mMap.setInfoWindowAdapter(infoWindowAdapter);

        // FusedLocationProviderClient 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // 위치 권한 체크 및 요청
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            // 위치 권한이 있는 경우 현재 위치 가져오기
            getLastLocation();
            mMap.setMyLocationEnabled(true);
        }
        // getPlaceListFromServer() 메서드를 호출하기 전에 mMap 객체가 초기화되었는지 확인
        if (mMap != null) {
            getPlaceListFromServer();


        }
        if (mMap == null) {
            Log.d("MapActivity", "mMap is null");
        }
    }
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                if (cameraPosition != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition, 18));
                    Toast.makeText(MapActivity.this, "저장된 위치로 이동", Toast.LENGTH_SHORT).show();
                } else {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    Toast.makeText(MapActivity.this, "현재 위치로 이동", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        // 마커 클릭 시 InfoWindow 표시
        marker.showInfoWindow();
        return true;
    }
}