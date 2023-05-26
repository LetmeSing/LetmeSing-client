package com.example.letmesing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtv_id;
    private EditText edtv_password;
    private Button btn_login_post;
    private TextView tv_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtv_id = findViewById(R.id.editText_login_id);
        edtv_password = findViewById(R.id.editText_login_password);
        btn_login_post = findViewById(R.id.button_login_post);
        tv_message = findViewById(R.id.textView_login_message);

        btn_login_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = edtv_id.getText().toString();
                String password = edtv_password.getText().toString();

                LoginDM loginDM = new LoginDM(id, password);
                Call call2 = RetrofitClient.getApiService().login_api_post(loginDM);
                call2.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (!response.isSuccessful()) {
                            Log.e("연결 비정상", Integer.toString(response.code()));
                            tv_message.setText("로그인에 실패했습니다.");
                            return;
                        }
                        Log.d("연결 성공 response.toStr", response.toString());
                        Log.d("연결 성공 .header", response.headers().toString());
                        Log.d("연결 성공 .body", response.body().toString());
                        Log.d("연결 성공 .raw", response.raw().toString());
                        Log.d("연결 성공 Gson.tojson(.body)", new Gson().toJson(response.body()));

                        // Server API 의 response 에서 body 를 Json 형태로 받기
                        String str_json = new Gson().toJson(response.body());
                        JSONObject json = null;
                        try {
                            json = new JSONObject(str_json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        // Json 안의 "user" Json 추출
                        JSONObject json_user = json.optJSONObject("user");
                        UserInfo userinfo = new Gson().fromJson(json_user.toString(), UserInfo.class);
                        // user json 추출 확인용
                        // json.getString 은 없으면 Exception 발생, optString 은 없으면 "" 반환
                        Log.d("Json 확인", json_user.toString());

                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                        // 나중에 is_master T/F 로 바뀌면 주석 해제 할것
//                        if(Boolean.parseBoolean(userinfo.getIs_master())) myIntent = new Intent(LoginActivity.this, MasterActivity.class);
                        if(userinfo.getIs_master().compareTo("0") == 0) myIntent = new Intent(LoginActivity.this, MasterActivity.class);
                        // 일반 사용자 > Main activity
                        myIntent.putExtra("user info", userinfo);
                        startActivity(myIntent);
                        finish();
                    }
                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.v("연결 실패", t.getMessage());
                        tv_message.setText("로그인에 실패했습니다.");
                        return;
                    }
                });
            }
        });
    }
//    Back pressed > parent activity 로 이동
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = this.getParentActivityIntent();
        startActivity(myIntent);
        finish();
    }
}

// 로그인 user 정보 저장용  intent 로 전달하기 위해서 Serializable implement 함
class UserInfo implements Serializable {
    private String id;              // DB 내에서의 user PK
    private String password;
    private String last_login;
    private String login_id;        // user 가 login 에 사용한 id
    private String is_superuser;
    private String is_active;
    private String is_staff;
    private String is_master;       // 점주:1 일반사용자:0
    private String nickname;        // 닉네임
    private String[] groups;
    private String[] user_permissions;

    public UserInfo (String id, String password, String last_login, String login_id, String is_superuser, String is_active, String is_staff, String is_master, String nickname, String[] groups, String[] user_permissions) {
        this.id = id;
        this.password = password;
        this.last_login = last_login;
        this.login_id = login_id;
        this.is_superuser = is_superuser;
        this.is_active = is_active;
        this.is_staff = is_staff;
        this.is_master = is_master;
        this.nickname = nickname;
        this.groups = groups;
        this.user_permissions = user_permissions;
    }

    public String getLogin_id() {
        return login_id;
    }

    public String getIs_superuser() {
        return is_superuser;
    }

    public String getIs_active() {
        return is_active;
    }

    public String getIs_staff() {
        return is_staff;
    }

    public String getIs_master() {
        return is_master;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAll() {
        String str = "id: " + id + "\nlogin_id: " + login_id + "\nis_superuser: " + is_superuser + "\nis_active: " + is_active + "\nis_staff: " + is_staff + "\nis_master: " + is_master + "\nnickname: " + nickname;
        return str;
    }
}