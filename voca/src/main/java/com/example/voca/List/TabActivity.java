package com.example.voca.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.voca.MainActivity;
import com.example.voca.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TabActivity extends AppCompatActivity implements VocaDialog.VocaDialogListener {
    private static final String TAG = "TabActivity";

    Map<String, Object> mapKey;


    // 파이어베이스에서 데이터 저장 및 읽어올 부분 연결할 객체들 생성
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private ArrayList<TabListItem> vocaName;
    private RecyclerView recyclerView;
    private TabListAdapter adapter;
    private ItemTouchHelper tItemTouchHelper;

    //floatingactionbtn

    private FloatingActionButton fabMain;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabSearch;

    // 플로팅버튼 상태
    private boolean fabMain_status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);


        // 실시간 사용자의 정보 받아옴
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        // 실시간 데이터베이스 연결
        firebaseDatabase = FirebaseDatabase.getInstance(); // 실시간 DB 관리 객체 얻어오기
        mDatabaseReference = firebaseDatabase.getReference("users").child(uid).child("userVoca"); // 저장시킬 노드 참조객체 가져오기 -> DB/users/uid/userVoca
        Log.d(TAG, "mDatabaseReference: " + mDatabaseReference );

        //findViewById,버튼 매핑
        fabMain = findViewById(R.id.fabMain);
        fabEdit = findViewById(R.id.fabEdit);
        fabSearch = findViewById(R.id.fabSearch);
        recyclerView = (RecyclerView) findViewById(R.id.rv_tab_list);


        // 리사이클러뷰 설정
        recyclerView = findViewById(R.id.rv_tab_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        //액션바
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        vocaName = new ArrayList<>();

        // 리스너: 별도의 읽어오기 버튼 없음 -> DB 변경이 발생하면 이에 반응하는 리스너를 통해 DB 읽어옴
        // 리스너 연결 -> DB/users/uid/userVoca
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "addValueEventListener: getChildren() " + dataSnapshot.getChildrenCount());
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String title = snapshot.getKey().toString();
                    TabListItem item = new TabListItem(title);

                    // 즐겨찾기 단어장
                    if(title.equals("star"))
                    {
                        item.setName("즐겨찾는 단어장");

                        // 즐겨찾기 단어장 내에 단어가 하나도 없으면 화면에 나타나지 않음
                        if(0 != snapshot.getChildrenCount())
                        {
                            vocaName.add(0, item); // 즐겨찾기는 항상 화면 맨 위에 존재
                        }
                        continue;
                    }

                    vocaName.add(item); // DB내에 저장되어 있는 사용자 지정 단어장은 모두 vocaName에 ArrayList 로저장됨

                    Log.d(TAG, "title: " + title);
                    Log.d(TAG, "add item -> size: " + vocaName.size());
                }



                // 어댑터 연결
                adapter = new TabListAdapter(TabActivity.this, R.layout.tablist_item, vocaName);
                //vocaDetailAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

                tItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
                tItemTouchHelper.attachToRecyclerView(recyclerView);

            }




            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        // adapter.setItems(vocaName);

        fabMain.setOnClickListener(view -> toggleFab());


        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    Intent intent2 = new Intent(getApplicationContext(), VocaSearchActivity.class);
                    startActivity(intent2);
                }

            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

    }


    public void openDialog() {
        VocaDialog vocaDialog = new VocaDialog();
        vocaDialog.show(getSupportFragmentManager(), "voca dialog");
    }

    // 플로팅 액션 버튼 클릭시 애니메이션 효과
    public void toggleFab() {
        if (fabMain_status) {
            // 플로팅 액션 버튼 닫기
            // 애니메이션 추가
            ObjectAnimator fc_animation = ObjectAnimator.ofFloat(fabSearch, "translationY", 0f);
            fc_animation.start();
            ObjectAnimator fe_animation = ObjectAnimator.ofFloat(fabEdit, "translationY", 0f);
            fe_animation.start();
            // 메인 플로팅 이미지 변경
            fabMain.setImageResource(R.drawable.ic_baseline_add_24);

        } else {
            // 플로팅 액션 버튼 열기
            ObjectAnimator fc_animation = ObjectAnimator.ofFloat(fabSearch, "translationY", -200f);
            fc_animation.start();
            ObjectAnimator fe_animation = ObjectAnimator.ofFloat(fabEdit, "translationY", -400f);
            fe_animation.start();
            // 메인 플로팅 이미지 변경
            fabMain.setImageResource(R.drawable.ic_baseline_close_24);
        }
        // 플로팅 버튼 상태 변경
        fabMain_status = !fabMain_status;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override

    // 사용자가 vocaname이라는 새 단어장 추가
    public void applyTexts(String vocaname) {
        boolean isSame = false;

//        for(TabListItem voca: vocaName)
//        {
//            // 이미 동일한 단어장이 존재하면 생성되지 않음
//            if(voca.equals(vocaname)){
//                isSame = true;
//                Toast.makeText(getApplicationContext(), "동일한 이름의 단어장이 존재합니다", Toast.LENGTH_SHORT).show();
//                break;
//            }
//        }

        if(!isSame) {
            TabListItem item = new TabListItem(vocaname);

            mapKey = new HashMap<>();
            mapKey.put(vocaname, 0);

            // 현재 루트: DB/users/uid/userVoca
            mDatabaseReference.updateChildren(mapKey);
            vocaName.add(item);
            //adapter.notifyDataSetChanged();
        }
    }
}