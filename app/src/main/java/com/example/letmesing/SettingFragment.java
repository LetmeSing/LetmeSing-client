package com.example.letmesing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment {
    private UserInfo userinfo;
    private TextView tv_userinfo;
    private TextView tv_userName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);

        // Userinfo 객체 전달 받음
        Bundle mybundle = getArguments();
        if (mybundle != null) {
            userinfo = (UserInfo) mybundle.get("user info");
        }
        tv_userName = (TextView) rootView.findViewById(R.id.textView_userNickname);
        tv_userName.setText(String.valueOf(userinfo.getNickname()) + "님은");
        tv_userinfo = (TextView) rootView.findViewById(R.id.textView_userinfo);
        if (userinfo.getIs_master().compareTo("0")==0) {
            tv_userinfo.setText("일반 사용자 입니다.");
        }
//        tv_userinfo.setText(userinfo.getAll());

        return rootView;
    }
}