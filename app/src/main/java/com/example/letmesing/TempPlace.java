package com.example.letmesing;

//해당 필드를 직렬화하거나 역직렬화할 때 사용할지 여부를 지정합니다. 지정되지 않은 필드는 Gson이 무시하고 처리하지 않습니다.
import com.google.gson.annotations.Expose;

//Java 객체의 필드와 JSON 데이터의 속성 간의 이름 매핑을 지정합니다. 객체의 필드 이름과 JSON 데이터의 속성 이름이 다를 때 매핑을 정의할 수 있습니다.
import com.google.gson.annotations.SerializedName;


public class TempPlace {
    //    serializer db 에 저장된 data 는 row 형태 > 1번 row 를 뽑아줄때 byte 의 나열로 제공됨 > serializer 는 해당 DB 의 row 를 json 형태로 가공하는 method
    //    deSerializer > 반대 작업
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("remainingSeat")
    @Expose
    private int remainingSeat;
    @SerializedName("totalSeat")
    @Expose
    private int totalSeat;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;

    public TempPlace() {
    }

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getAddress(){
        return address;
    }
    public int getRemainingSeat(){
        return remainingSeat;
    }
    public int getTotalSeat(){
        return totalSeat;
    }
    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
}