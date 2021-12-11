package com.example.voca.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.voca.R;
import com.example.voca.realtimeDB.VocaVO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// 제공해주는 단어장 목록 화면(수능->week1 클릭시 나오는 단어 목록 화면)
// VocaDetailActivity를 사용하지 않은 이유:
// 단어목록을 나타내기 위해서는 파이어베이스에서 데이터를 가져와야하는데
// 파이어베이스 내에 '사용자가 저장한 단어장'과 '우리가 제공해주는 단어장'의 저장 위치가 다르기 때문에
// 이를 가져오는 코드도 다를 수 밖에 없음 -> 그래서 따로 분리
public class VocaDetailProvidedActivity extends AppCompatActivity {
    private static final String TAG_ACT = "VocaDetailProvidedActivity";
    private static final String TAG_DB = "ReadData_providedVoca";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userDB;
    private DatabaseReference mDatabaseReference; // 데이터베이스의 주소 저장
    private DatabaseReference mDatabase;

    ValueEventListener mValueEventListener; // 리스너 선언

    String subTitle;
    String subName;

    RecyclerView recyclerView;
    VocaDetailProvidedAdapter vocaDetailAdapter;
    ArrayList<VocaDetailProvidedItem> items = new ArrayList<>(); // 받아온 영어, 한글 담기 위함

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG_ACT, "onCreate start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_detail_provided);

        // name, subName 은 DB 내의 단어장 이름
        Intent intent = getIntent();
        String title = intent.getExtras().getString("title");
        subTitle = intent.getExtras().getString("subTitle"); // 액션바 부분에 쓰일 단어장 제목 받아옴
        String name = intent.getExtras().getString("name");
        subName = intent.getExtras().getString("subName");

        // 리사이클러뷰 설정
        recyclerView = findViewById(R.id.rv_detail_provided_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        // 실시간 데이터베이스 연결
        firebaseDatabase = FirebaseDatabase.getInstance(); // 실시간 DB 관리 객체 얻어오기
        mDatabaseReference = firebaseDatabase.getReference("vocabulary"); // 저장시킬 노드 참조객체 가져오기 -> vocavulary
        Log.d(TAG_DB, "ReadData_ProvidedVoca: " + mDatabaseReference );
        mDatabase = mDatabaseReference.child(name).child(subName); // 불러올 데이터베이스의 주소
        Log.d(TAG_DB, "ReadData_ProvidedVoca: " + mDatabase );


        // 리스너: 별도의 읽어오기 버튼 없음 -> DB 변경이 발생하면 이에 반응하는 리스너를 통해 DB 읽어옴
        // 리스너 연결
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG_DB, "addValueEventListener: getChildren() " + dataSnapshot.getChildrenCount());
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) { // Eng 값이 각각의 직계 자손으로 인식
                    String Eng = snapshot.getKey().toString();
                    String Kor = snapshot.getValue().toString();

                    Log.d(TAG_DB, "addValueEventListener: Key " + Eng);
                    Log.d(TAG_DB, "addValueEventListener: value " + Kor);


                    VocaDetailProvidedItem item = new VocaDetailProvidedItem(Eng, Kor);
                    items.add(item);
                    Log.d(TAG_DB, "add item1 -> size:" + items.size()); //
                }

                // 어댑터 연결
                vocaDetailAdapter = new VocaDetailProvidedAdapter(VocaDetailProvidedActivity.this, R.layout.vocasearch_sub_item, items);
                //vocaDetailAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(vocaDetailAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d(TAG_DB, "add item2 -> size:" + items.size());


        //액션바
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(subTitle); // 액션바의 제목 부분
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    // 옵션 메뉴 사용
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_option, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:{ // 상단 좌측 키 눌렀을 때 동작
                finish();
                return true;
            }

            case R.id.provided_voca_menu:{ // 상단 우측 키 눌렀을 때 동작
                readData();
                Toast.makeText(getApplicationContext(),"내 단어장에 추가되었습니다", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(VocaDetailProvidedActivity.this, TabActivity.class);
                startActivity(intent);

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    // 선택된 단어장 사용자 단어장에 저장
    private void readData()
    {
        VocaVO vocaVO;

        Map<String, Object> map = new HashMap<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        userDB = firebaseDatabase.getReference("users").child(uid).child("userVoca");
        map.put(subTitle, 0);
        userDB.updateChildren(map);
        userDB = userDB.child(subTitle);

        for(VocaDetailProvidedItem item : items)
        {
            vocaVO = new VocaVO(item.getEng(), item.getKor(), false, false);
            userDB.updateChildren(vocaVO.toMap());
        }

    }
}


