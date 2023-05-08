package com.example.letmesing;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private List<Place> placeList;
    private RecyclerView recyclerView;
    private PlaceAdapter placeAdapter;
    private Call<List<Place>> call;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // 지도 fragment를 가져와서 비동기적으로 맵을 준비합니다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // 장소 목록 초기화
        placeList = new ArrayList<>();

        // Retrofit을 사용하여 API 호출
        call = RetrofitClient.getApiService().seat_api_get("1");
        call.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                if (response.isSuccessful()) {
                    // 서버에서 받은 장소 목록을 가져옴
                    placeList = response.body();

                    // 마커 생성 및 추가
                    for (Place place : placeList) {
                        double latitude = place.getLatitude();
                        double longitude = place.getLongitude();
                        LatLng latLng = new LatLng(latitude, longitude);
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(latLng)
                                .title(place.getName())
                                .snippet(place.getAddress());
                        Marker marker = mMap.addMarker(markerOptions);
                        marker.setTag(place);
                    }

                    // RecyclerView 초기화
                    recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MapActivity.this));
                    placeAdapter = new PlaceAdapter(placeList);
                    recyclerView.setAdapter(placeAdapter);
                }
                else{
                    Toast.makeText(MapActivity.this, "API 요청에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                // API 호출 실패 처리
            }
        });
    }

                    //LatLng latLng1 = new LatLng(37.507063, 126.958612);
                    //Place place1 = new Place("슈퍼스타코인노래연습장", "서울특별시 동작구 흑석동 195-30번지", R.drawable.karaoke1, latLng1);
                    //    placeList.add(place1);

                    //LatLng latLng2 = new LatLng(37.507340, 126.959159);
                    //Place place2 = new Place("비트코인동전노래방", "서울특별시 동작구 흑석동 번지 지층 190-33", R.drawable.karaoke2, latLng2);
                    //    placeList.add(place2);

                    //LatLng latLng3 = new LatLng(37.507021, 126.958560);
                    //Place place3 = new Place("잇츠코인노래방", "서울특별시 동작구 흑석동 195-17번지 3층", R.drawable.karaoke3, latLng3);
                    //    placeList.add(place3);
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //지도초기 위치 설정
        LatLng karaoke0 = new LatLng(37.507063, 126.958612);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(karaoke0, 18));

        //마커 클릭 이벤트 처리
        mMap.setOnMarkerClickListener(this);

        // 정보창 인터페이스
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null; // 기본 정보창 사용
            }

            @Override
            public View getInfoContents(Marker marker) {
                // 커스텀 정보창을 위한 View 객체 생성
                View infoWindowView = getLayoutInflater().inflate(R.layout.custom_info_window, null);

                // View 내부의 요소들을 찾아옴
                ImageView placeImageView = infoWindowView.findViewById(R.id.placeImageView);
                TextView titleTextView = infoWindowView.findViewById(R.id.titleTextView);
                TextView snippetTextView = infoWindowView.findViewById(R.id.snippetTextView);

                // 마커에 설정된 태그를 가져옴
                Object tag = marker.getTag();

                if (tag instanceof Place) {
                    // 마커의 태그로 설정된 Place 객체를 가져옴
                    Place place = (Place) tag;

                    // Place 객체에서 이름, 주소, 사진 리소스 ID를 가져와서 View에 표시
                    titleTextView.setText(place.getName());
                    snippetTextView.setText(place.getAddress());

                    // 장소의 사진 리소스 ID를 가져와서 ImageView에 설정
                    //int photoResId = place.getPhotoResId();
                    //placeImageView.setImageResource(photoResId);
                }

                return infoWindowView;
            }
        });
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        // 마커를 클릭했을 때 팝업 정보 띄우기
        marker.showInfoWindow();
        return true;
    }


}