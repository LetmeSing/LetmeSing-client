package com.example.letmesing;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AddAlbumFragment extends Fragment {
    Button btn_post_album;
    EditText edtv_ablum_name;
    EditText edtv_album_description;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_album, container, false);

        btn_post_album = (Button) rootView.findViewById(R.id.button_postAlbum);
        btn_post_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtv_ablum_name = (EditText) rootView.findViewById(R.id.editText_album_name);
                edtv_album_description = (EditText) rootView.findViewById(R.id.editText_album_description);
                String name = edtv_ablum_name.getText().toString();
                String description = edtv_album_description.getText().toString();
                AlbumDM data = new AlbumDM();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }
}