package com.example.letmesing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    private TextView tv_title;
    private Button btn_login;
    private TextView tv_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        tv_title = (TextView) findViewById(R.id.textView_title);
        btn_login = (Button) findViewById(R.id.button_login);
        tv_signin = (TextView) findViewById(R.id.textView_signin);

        Spannable span = (Spannable) tv_signin.getText();
        tv_signin.setMovementMethod(LinkMovementMethod.getInstance());
        span.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                // click
                Intent myIntent = new Intent(StartActivity.this, SigninActivity.class);
                startActivity(myIntent);
                finish();
            }
        }, 12, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_signin.setMovementMethod(LinkMovementMethod.getInstance());

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
    }
}