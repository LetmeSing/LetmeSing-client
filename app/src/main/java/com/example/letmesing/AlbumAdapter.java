package com.example.letmesing;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumAdapter extends ArrayAdapter {
    //  Album 리스트를 생성하기 위한 adapter
    private Context context;
    private List albumList;
    AlbumFragment albumfragment; // adapter 를 호출한 fragment > fragment 이동 구현하기 위해 받아옴

    class ViewHolder {
        public TextView tv_name;
        public TextView tv_description;
        public TextView tv_createdDate;
        public TextView tv_numSongs;
        public CardView cv_album;
        public ImageView iv_delete;
    }

    public AlbumAdapter (Context context, List albumList, AlbumFragment albumfragment) {
        super(context, 0, albumList);
        this.context = context;
        this.albumList = albumList;
        this.albumfragment = albumfragment;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final AlbumAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.album_item, parent, false);
        }

        viewHolder = new AlbumAdapter.ViewHolder();
        viewHolder.tv_name = (TextView) convertView.findViewById(R.id.textView_albumName);
        viewHolder.tv_description = (TextView) convertView.findViewById(R.id.textView_description);
        viewHolder.tv_createdDate = (TextView) convertView.findViewById(R.id.textView_createdDate);
        viewHolder.tv_numSongs = (TextView) convertView.findViewById(R.id.textView_numSongs);
        viewHolder.cv_album = (CardView) convertView.findViewById(R.id.cardView_album);
        viewHolder.iv_delete = (ImageView) convertView.findViewById(R.id.imageView_album_delete);

        final AlbumItem albumItem = (AlbumItem) albumList.get(position);
        viewHolder.tv_name.setText(albumItem.getName());
        viewHolder.tv_description.setText(albumItem.getDescription());
        viewHolder.tv_createdDate.setText(albumItem.getCreated_at());
//        viewHolder.tv_numSongs.setText(albumItem.getNumOfSongs());

        // clickable 이 달려있는 cardview 를 클릭 시 list fragment 로 frag 전환 + 해당 album의 id 값을 list fragment 에 전달
        // 구현 필요: 선택된 album 의 id 값을 list Fragment 전달 > 해당 id 값을 가진 Music 들로 구성된 List frag 를 생성 >> 생성자로 전달?
        viewHolder.cv_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = albumfragment.getActivity().getSupportFragmentManager().beginTransaction();
                // 확인 필요: 이거 매번 new 로 생성되면 수정한 결과가 반영 안되는거 아닌가? > 아 어차피 DB 에서 받아오는거면 문제없을수도? > 문제 없더라
                // + bundle 활용해서 값 전달 구현 필요
                MusicFragment mylistFragment = new MusicFragment(albumItem.getId());  // 이거 albumItem 가 CardView 에서의 position 값이면 조금 곤란할 수 있음 > 정렬이 흐트러지면 다른거 클릭하게됨
                //main_layout에 homeFragment로 transaction 한다.
                transaction.replace(R.id.layout_main, mylistFragment);
                //꼭 commit을 해줘야 바뀐다.
                transaction.addToBackStack(null).commit();
//                Toast.makeText(getContext(), albumItem.getId() + "번 ID의 앨범이 클릭되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), (position+1)+"번째 아이템이 삭제됩니다.", Toast.LENGTH_SHORT).show();
                String id_db = ((AlbumItem) albumList.get(position)).getId();
                Log.d("DELETE: Album 확인", "UI id : " + position + "\n실제 DB id : " + id_db);
                delete_album(id_db);
                albumList.remove(position);
                notifyDataSetChanged();
            }
        });
        // image 나 button 등에 Tag를 사용해서 position 을 부여해 준다.
        // Tag란 View를 식별할 수 있게 바코드 처럼 Tag를 달아 주는 View의 기능
        viewHolder.tv_name.setTag(albumItem.getName());
        //Return the completed view to render on screen
        return convertView;
    }
    private void delete_album (String id_db) {
        // DB 에 Album instance 삭제 함수
        // UI 에 띄워주는거는 notifyDataSetChanged(); 로 끝났으니 sync 처리할 필요 없음
        Call<Void> call = RetrofitClient.getApiService().album_api_delete(id_db);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Log.d("DELETE: Album: Not Success", "Not Success: code " + response.message());
                    return;
                }
                Log.d("DELETE: Album: Success", "Success: code " + response.code());
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("DELETE: Album: Error", "실패 " + t.getMessage());
            }
        });
    }
}
