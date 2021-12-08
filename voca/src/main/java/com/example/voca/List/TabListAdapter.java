package com.example.voca.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voca.R;
import com.example.voca.VocaList.VocaListActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TabListAdapter extends RecyclerView.Adapter<TabListAdapter.ItemViewHolder> implements ItemTouchHelperListener {
    // 파이어베이스에서 데이터 저장 및 읽어올 부분 연결할 객체들 생성
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    Context context;
    int resId;
    ArrayList<TabListItem> items;


    public TabListAdapter() {
    }

    public TabListAdapter(Context context, int resId, ArrayList<TabListItem> items) {
        this.context = context;
        this.items = items;
        this.resId = resId;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.tablist_item, parent, false);
        return new ItemViewHolder(view);
    }

    //아이템 클릭 리스너 인터페이스
    interface OnItemClickListener{
        void onItemClick(View v, int position); //뷰와 포지션값
    }
    //리스너 객체 참조 변수
    private TabListAdapter.OnItemClickListener mListener = null;
    //리스너 객체 참조를 어댑터에 전달 메서드
    public void setOnItemClickListener(TabListAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }



    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(items.get(position), position);
        String title = items.get(position).getName();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            //recyclerview의 item 클릭 시에 학습 화면으로 이동
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), VocaListActivity.class);
                intent.putExtra("title", title); // 선택된 단어장 이름 액션바에 나타내기

                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<TabListItem> itemList) {
        items = itemList;
        notifyDataSetChanged();
    }

    @Override
    // TODO: 선택된 버튼이 눌렸다는 느낌이 나게 하기 -> 선택된 텍스트가 진해진다든지, 커진다든지 등의 방법 이용
    public boolean onItemMove(int form_position, int to_position) {
        TabListItem item = items.get(form_position);
        items.remove(form_position);
        items.add(to_position, item);
        // item.setNumber(to_position);
        notifyItemMoved(form_position, to_position);
        return true;
    }

    @Override
    // TODO:스와이프시 삭제하겠냐는 다이얼로그 띄운 뒤, 사용자가 오케이하면 해당 단어장 삭제
    public void onItemSwipe(int position) {
        boolean isRemove = true;
        // TODO: 이곳에 다이얼로그 코드 작성


        isRemove = removeDB(position); // 해당하는 단어장 DB에서 삭제
        if(isRemove) {
            items.remove(position);
            notifyItemRemoved(position);
        }
        else{ // '즐겨찾는 단어장'을 삭제하려고 할 때
            Toast.makeText(context, "로그인 성공", Toast.LENGTH_LONG).show();
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tabName;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tabName = itemView.findViewById(R.id.tab_list_item);
        }

        public void onBind(TabListItem item, int position) {
            tabName.setText(item.getName());
            //item.setNumber(position);
        }
    }

    private boolean removeDB(int position)
    {
        String removeTitle = items.get(position).getName();
        if(removeTitle.equals("즐겨찾는 단어장")){
            return false; // 즐겨찾는 단어장은 삭제하지 못하도록 함
        }

        // 실시간 사용자의 정보 받아옴
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        // 실시간 데이터베이스 연결
        firebaseDatabase = FirebaseDatabase.getInstance(); // 실시간 DB 관리 객체 얻어오기
        mDatabaseReference = firebaseDatabase.getReference("users").child(uid).child("userVoca").child(removeTitle); // 저장시킬 노드 참조객체 가져오기 -> DB/users/uid/userVoca/삭제할단어장

        mDatabaseReference.setValue(null);

        return true;
    }
}