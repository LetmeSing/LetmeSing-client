package com.example.letmesing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

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
                Call<LoginDM> call2 = RetrofitClient.getApiService().login_api_post(loginDM);
                call2.enqueue(new Callback<LoginDM>() {
                    @Override
                    public void onResponse(Call<LoginDM> call, Response<LoginDM> response) {
                        if (!response.isSuccessful()) {
                            Log.e("연결 비정상", Integer.toString(response.code()));
                            tv_message.setText("로그인에 실패했습니다.");
                            return;
                        }
                        LoginDM RegisterResponse = response.body();
                        Log.d("연결 성공", response.body().toString());
                        // 로그인 성공 > Main Activity 이동
                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(myIntent);
                        finish();
                    }
                    @Override
                    public void onFailure(Call<LoginDM> call, Throwable t) {
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