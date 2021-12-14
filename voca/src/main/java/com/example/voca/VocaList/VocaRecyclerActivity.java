package com.example.voca.VocaList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voca.List.ItemTouchHelperCallback;
import com.example.voca.List.TabActivity;
import com.example.voca.Memorize.Basic.MemorizeActivity;
import com.example.voca.Memorize.MultiChoice.MultiChoiceActivity;
import com.example.voca.Memorize.SpellCheck.SpellCheckActivity;
import com.example.voca.R;
import com.example.voca.realtimeDB.VocaVO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

// TODO: 즐겨찾는 단어장인 경우 우측 상단에 탭을 넣지 않음 -> 즐겨찾기 단어장에서는 단어를 추가하지 않을 것이므로
public class VocaRecyclerActivity extends AppCompatActivity implements VocaDialogVocaList.VocaDialogVocaListListener {
    public static final String TAG = "VocaListActivity";

    // 파이어베이스에서 데이터 저장 및 읽어올 부분 연결할 객체들
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase;

    RecyclerView recyclerView;
    VocaRecyclerAdapter vocaRecyclerAdapter;
    ItemTouchHelper itemTouchHelper;

    ArrayList<VocaVO> vocaData = new ArrayList<>(); // 영어,한글,암기여부,즐찾여부

    int count; // 단어 암기 여부 개수 카운트하기 위해 디비에서 카운트 가져옴
    String title; // DB에서 단어장 주소 찾기 위해 사용
    String today = getToday(); // DB에서 오늘의 count 받아오기 위해 사용

    // 학습화면으로 넘어가는 버튼들
    Button memorizeBtn;
    Button choiceBtn;
    Button spellCheckBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_list);

        // 인텐트 받아옴
        Intent intent = getIntent();
        title = intent.getStringExtra("title"); // 액션바 부분에 쓰일 단어장 제목 받아옴
        Log.d(TAG, "title: " + title);

        // 액션바
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title); // 액션바의 제목 부분
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // 리사이클러뷰 설정
        recyclerView = findViewById(R.id.voca_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        // 실시간 사용자의 정보 받아옴
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        // 실시간 데이터베이스 연결
        firebaseDatabase = FirebaseDatabase.getInstance(); // 실시간 DB 관리 객체 얻어오기
        if(title.equals("즐겨찾는 단어장")){ // '즐겨찾는 단어장'의 경우, DB내 단어장 이름과 화면 내 단어장 이름이 다름
            title = "star"; // DB내에서는 star라고 저장되어있음
        }
        mDatabase = firebaseDatabase.getReference("users").child(uid).child("userVoca").child(title); // 저장시킬 노드 참조객체 가져오기 -> DB/users/uid/userVoca/선택한 단어장
        Log.d(TAG, "mDatabaseReference: " + mDatabase );

        // 리스너: 별도의 읽어오기 버튼 없음 -> DB 변경이 발생하면 이에 반응하는 리스너를 통해 DB 읽어옴
        // 리스너 연결 -> DB/users/uid/userVoca/선택된 단어장
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "addValueEventListener: getChildren() " + dataSnapshot.getChildrenCount());
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    VocaVO voca = new VocaVO();
                    voca.setVocaEng(snapshot.getKey()); // 영어단어를 voca.vocaEng에 저장함
                    Log.d(TAG, "addValueEventListener: getKey() " + voca.getVocaEng());

                    for (DataSnapshot snapshot_child: snapshot.getChildren()){
                        String value = snapshot_child.getValue().toString();
                        Log.d(TAG, "addValueEventListener: getvalue() " + value);
                        switch (snapshot_child.getKey())
                        {
                            case "vocaKor":
                                voca.setVocaKor(value); // 뜻을 voca.vocaKor에 저장함
                                break;
                            case "memoCheck":
                                voca.setMemoCheck(Boolean.parseBoolean(value)); // 암기여부를 voca.memoCheck에 저장함
                                break;
                            case "starCheck":
                                voca.setStarCheck(Boolean.parseBoolean(value)); // 즐겨찾기여부를 voca.starCheck에 저장함
                                break;
//                            case "title":
//                                voca.setVocaTitle(value);
//                                break;
                        }

                    }

                    vocaData.add(voca); // 영어단어,뜻,암기여부,즐겨찾기여부를 모두 담은 voca를 vocaData Array List에 저장함

                    getCount(uid); // 현재 암기한 개수(count)를 받아옴

                    // 어댑터 연결
                    vocaRecyclerAdapter = new VocaRecyclerAdapter(VocaRecyclerActivity.this, R.layout.vocasearch_sub_item, vocaData, title, count, today);
                    recyclerView.setAdapter(vocaRecyclerAdapter);

                    // 스와이프 동작 위함
                    itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(vocaRecyclerAdapter));
                    itemTouchHelper.attachToRecyclerView(recyclerView);

                    clickButton(vocaData); // 학습버튼 관련 setOnClickListener 모음 -> 리스너가 값을 받아올 때마다 인텐트로 보내는 vocaData가 실시간으로 달라지므로 리스너 내에 존재해야함


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        memorizeBtn=findViewById(R.id.btn_memorize); //암기화면 이동 버튼
        choiceBtn=findViewById(R.id.btn_choice); //객관식 화면 이동 버튼
        spellCheckBtn=findViewById(R.id.btn_spellcheck); //스펠 체크 화면 이동 버튼
    }

    // 옵션 메뉴 사용
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_voca_list,menu);
        return true;
    }

    // 옵션 메뉴 선택했을때
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // 상단 좌측 키(back 키) 눌렀을 때 동작
            case android.R.id.home:{
                finish();//인텐트 종료
                Intent intent = new Intent(VocaRecyclerActivity.this, TabActivity.class); //인텐트
                startActivity(intent); //액티비티 열기

                break;
            }
            // 상단 우측 키의 "단어 추가" 눌렀을 때 동작 -> 단어 추가 다이얼로그 생성
            case R.id.voca_add:
                openDialog();
                break;
        }

        return true;
    }

    // 리스너가 값을 받아올 때마다 사용되는 vocaData가 실시간으로 달라지므로 리스너 내에 존재해야함
    private void clickButton(ArrayList<VocaVO> vocaData)
    {
        // 암기 화면 이동 버튼
        memorizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VocaRecyclerActivity.this, MemorizeActivity.class);
                Bundle bundleData=new Bundle();
                bundleData.putParcelableArrayList("vocaData",(ArrayList<VocaVO>) vocaData);
                intent.putExtra("vocaData",bundleData);
                intent.putExtra("title", title);
                //intent.putParcelableArrayListExtra("vocaData",(ArrayList<VocaVO>) vocaData);
                startActivity(intent);
            }
        });

        // 객관식 화면 이동 버튼
        choiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(VocaRecyclerActivity.this, MultiChoiceActivity.class);
                Bundle bundleData=new Bundle();
                bundleData.putParcelableArrayList("vocaData",(ArrayList<VocaVO>) vocaData);
                intent1.putExtra("vocaData",bundleData);
                intent1.putExtra("title", title);
                //intent1.putParcelableArrayListExtra("vocaData",(ArrayList<VocaVO>) vocaData);
                startActivity(intent1);
            }
        });

        // 스펠링 체크 화면 이동 버튼
        spellCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(VocaRecyclerActivity.this, SpellCheckActivity.class);
                Bundle bundleData=new Bundle();
                bundleData.putParcelableArrayList("vocaData", vocaData);
                intent2.putExtra("vocaData",bundleData);
                intent2.putExtra("title", title);
                // intent2.putParcelableArrayListExtra("vocaData",(ArrayList<VocaVO>) vocaData);
                startActivity(intent2);
            }
        });
    }

    // 달성률 얻어오기
    private void getCount(String uid)
    {
        today = getToday();

        // mDatabase: DB/users/uid/goals/today/count
        DatabaseReference mDatabase = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("goals")
                .child(today)
                .child("count");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = dataSnapshot.getValue(Integer.class); // DB내 count(오늘의 단어 암기 개수) 값을 가져와 count에 저장
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // 오늘 날짜 계산
    private String getToday()
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);

        String today = sdf.format(date);

        return today;
    }

    // 단어 추가 관련 다이얼로그
    public void openDialog() {
        VocaDialogVocaList dialog = new VocaDialogVocaList(vocaData, VocaRecyclerActivity.this);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    // 사용자가 단어장에 새 단어 추가
    public void applyTexts(String eng, String kor) {
        VocaVO voca = new VocaVO(eng, kor, false, false); // eng, kor: 사용자가 입력한 영단어와 뜻, 암기여부와 즐찾여부는 false로 설정하여 DB에 단어 생성

        mDatabase.updateChildren(voca.toMap()); // 현재 루트(DB/users/uid/userVoca/선택된 단어장)에 바로 위의 voca라는 데이터 저장

        // 화면 갱신
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onRestart() {
        super.onRestart();

        // 화면 갱신
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}





