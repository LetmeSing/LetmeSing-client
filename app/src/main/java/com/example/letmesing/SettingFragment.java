package com.example.letmesing;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingFragment extends Fragment {
    private UserInfo userinfo;
    private TextView tv_userinfo;

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

        tv_userinfo = (TextView) rootView.findViewById(R.id.textView_userinfo);
        tv_userinfo.setText(userinfo.getAll());

        return rootView;
    }
}