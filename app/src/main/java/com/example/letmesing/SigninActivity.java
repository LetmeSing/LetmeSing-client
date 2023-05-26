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

public class SigninActivity extends AppCompatActivity {

    private EditText edtv_id;
    private EditText edtv_password;
    private EditText edtv_nickname;
    private CheckedTextView chktv_isMaster;
    private Button btn_singin_post;
    private TextView tv_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        edtv_id = findViewById(R.id.editText_siginin_id);
        edtv_password = findViewById(R.id.editText_siginin_password);
        edtv_nickname = findViewById(R.id.editText_siginin_nickname);
        chktv_isMaster = findViewById(R.id.checkedTextView_isMaster);
        btn_singin_post = findViewById(R.id.button_siginin_post);
        tv_message = findViewById(R.id.textView_signin_message);

        chktv_isMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CheckedTextView) view).toggle();
            }
        });

        btn_singin_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = edtv_id.getText().toString();
                String password = edtv_password.getText().toString();
                String nickname = edtv_nickname.getText().toString();
                String ismaster = "0";
                if(chktv_isMaster.isChecked())  ismaster = "1";

                RegisterDM registerDM = new RegisterDM(id, password, nickname, ismaster);
                Call<RegisterDM> call2 = RetrofitClient.getApiService().register_api_post(registerDM);
                call2.enqueue(new Callback<RegisterDM>() {
                    @Override
                    public void onResponse(Call<RegisterDM> call, Response<RegisterDM> response) {
                        if (!response.isSuccessful()) {
                            Log.e("연결 비정상", Integer.toString(response.code()));
                            tv_message.setText("회원가입에 실패했습니다.");
                            return;
                        }
                        RegisterDM RegisterResponse = response.body();
                        Log.d("연결 성공", response.body().toString());
                        // 회원가입 성공 > Start Activity 이동
                        Intent myIntent = getParentActivityIntent();
                        startActivity(myIntent);
                        finish();
                    }
                    @Override
                    public void onFailure(Call<RegisterDM> call, Throwable t) {
                        Log.v("연결 실패", t.getMessage());
                        tv_message.setText("회원가입에 실패했습니다.");
                        return;
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = this.getParentActivityIntent();
        startActivity(myIntent);
        finish();
    }
}