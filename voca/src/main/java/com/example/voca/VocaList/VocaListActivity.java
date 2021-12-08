package com.example.voca.VocaList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.example.voca.List.ItemTouchHelperCallback;
import com.example.voca.List.TabActivity;
import com.example.voca.List.TabListAdapter;
import com.example.voca.List.TabListItem;
import com.example.voca.List.VocaDialog;
import com.example.voca.List.VocaDialogVocaList;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

// TODO: TabListActivity 참고하여 리사이클러뷰로 변경 -> 스와이프시 메뉴 삭제 기능 구현하기 위해
public class VocaListActivity extends AppCompatActivity implements VocaDialogVocaList.VocaDialogVocaListListener{
    public static final String TAG = "VocaListActivity";
    public static Context vocaListActivityContext;

    // 파이어베이스에서 데이터 저장 및 읽어올 부분 연결할 객체들 생성
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseReference;

    ArrayList<VocaVO> vocaData;
    ListView vocaListView;

    int count;

    VocaListAdapter adapter;

    TextView eng;
    TextView kor;
    String title;

    String today = getToday();

    Button memorizeBtn;
    Button choiceBtn;
    Button spellCheckBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_list);

        Intent intent = getIntent();
        title = intent.getStringExtra("title"); // 액션바 부분에 쓰일 단어장 제목 받아옴
        Log.d(TAG, "title: " + title);

        //액션바
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title); // 액션바의 제목 부분
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        vocaListActivityContext=this;

        vocaListView=findViewById(R.id.voca_listview);
        // TODO: 화면 크기 벗어나는 단어 터치하면 or 터치 안해도 움직이게 설정 (notice_activity처럼)
//        eng = findViewById(R.id.eng);
//        kor = findViewById(R.id.kor);
//        eng.setSelected(true);
//        kor.setSelected(true);

        // 실시간 사용자의 정보 받아옴
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        // 실시간 데이터베이스 연결
        firebaseDatabase = FirebaseDatabase.getInstance(); // 실시간 DB 관리 객체 얻어오기

        // DB내 단어장 이름과 화면 내 단어장 이름이 다름
        if(title.equals("즐겨찾는 단어장")){
            title = "star";
        }
        mDatabaseReference = firebaseDatabase.getReference("users").child(uid).child("userVoca").child(title); // 저장시킬 노드 참조객체 가져오기 -> DB/users/uid/userVoca/선택한 단어장
        Log.d(TAG, "mDatabaseReference: " + mDatabaseReference );

        vocaData = new ArrayList<>();

        // 리스너: 별도의 읽어오기 버튼 없음 -> DB 변경이 발생하면 이에 반응하는 리스너를 통해 DB 읽어옴
        // 리스너 연결 -> DB/users/uid/userVoca/선택된 단어장
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "addValueEventListener: getChildren() " + dataSnapshot.getChildrenCount());
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    VocaVO voca = new VocaVO();
                    voca.setVocaEng(snapshot.getKey());
                    Log.d(TAG, "addValueEventListener: getKey() " + voca.getVocaEng());

                    for (DataSnapshot snapshot_child: snapshot.getChildren()){
                        String value = snapshot_child.getValue().toString();
                        Log.d(TAG, "addValueEventListener: getvalue() " + value);
                        switch (snapshot_child.getKey())
                        {
                            case "vocaKor":
                                voca.setVocaKor(value);
                                break;
                            case "memoCheck":
                                voca.setMemoCheck(Boolean.parseBoolean(value));
                                break;
                            case "starCheck":
                                voca.setStarCheck(Boolean.parseBoolean(value));
                                break;
                        }

                    }

                    vocaData.add(voca);

                    getCount(uid);

                    adapter = new VocaListAdapter(VocaListActivity.this, R.layout.voca_list_item, vocaData, title, count);
                    vocaListView.setAdapter(adapter);



                    memorizeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(VocaListActivity.this, MemorizeActivity.class);
                            Bundle bundleData=new Bundle();
                            bundleData.putParcelableArrayList("vocaData",(ArrayList<VocaVO>) vocaData);
                            intent.putExtra("vocaData",bundleData);
                            intent.putExtra("title", title);
                            //intent.putParcelableArrayListExtra("vocaData",(ArrayList<VocaVO>) vocaData);
                            startActivity(intent);
                        }
                    });


                    choiceBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent1=new Intent(VocaListActivity.this, MultiChoiceActivity.class);
                            Bundle bundleData=new Bundle();
                            bundleData.putParcelableArrayList("vocaData",(ArrayList<VocaVO>) vocaData);
                            intent1.putExtra("vocaData",bundleData);
                            intent1.putExtra("title", title);
                            //intent1.putParcelableArrayListExtra("vocaData",(ArrayList<VocaVO>) vocaData);
                            startActivity(intent1);
                        }
                    });


                    spellCheckBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent2=new Intent(VocaListActivity.this, SpellCheckActivity.class);
                            Bundle bundleData=new Bundle();
                            bundleData.putParcelableArrayList("vocaData", vocaData);
                            intent2.putExtra("vocaData",bundleData);
                            intent2.putExtra("title", title);
                            // intent2.putParcelableArrayListExtra("vocaData",(ArrayList<VocaVO>) vocaData);
                            startActivity(intent2);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //getListViewItem();//리스트뷰 아이템 가져오기
        //showListView();//리스트뷰 보여주기

//        adapter = new VocaListAdapter(this,R.layout.voca_list_item,vocaData);
//        vocaListView.setAdapter(adapter);



        //암기화면 이동 버튼
        memorizeBtn=findViewById(R.id.btn_memorize);


        //객관식 화면 이동 버튼
        choiceBtn=findViewById(R.id.btn_choice);


        //스펠 체크 화면 이동 버튼
        spellCheckBtn=findViewById(R.id.btn_spellcheck);


    }

//    private void getListViewItem(){
//        vocaData.add(new VocaVO("door","문",false,false));
//        vocaData.add(new VocaVO("cap","모자",false,false));
//        vocaData.add(new VocaVO("banana","바나나",false,false));
//        vocaData.add(new VocaVO("apple","사과",false,false));
//
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_voca_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
//            case R.id.time_sort:
//                item.setChecked(true);
//                vocaData.clear();
//                showData.clear();
//                getListViewItem();
//                showListView();
//                break;
//            case R.id.alpha_sort:
//                item.setChecked(true);
//                vocaData.clear();
//                showData.clear();
//                getListViewItem();
//                Collections.sort(vocaData);
//                showListView();
//                break;
            // TODO: 토스트 안보임
            case R.id.voca_add:
                openDialog();
                showToast("단어 추가");
                break;
//            case R.id.voca_remove:
//
//                showToast("단어 삭제");
//                break;

            //toolbar의 back키 눌렀을 때 동작
            case android.R.id.home:
                finish();
        }

        return true;
    }

    public void openDialog() {
        VocaDialogVocaList dialog = new VocaDialogVocaList(vocaData, VocaListActivity.this);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message,Toast.LENGTH_SHORT);
        toast.show();
    }
//    private void showListView(){
//        for(int i=0;i<vocaData.size();i++){
//            VocaVO vo=new VocaVO();
//            vo.memoCheck=vocaData.get(i).memoCheck;
//            vo.vocaEng= vocaData.get(i).vocaEng;
//            vo.vocaKor= vocaData.get(i).vocaKor;
//            vo.starCheck= vocaData.get(i).starCheck;
//            showData.add(vo);
//        }
//        VocaListAdapter adapter=new VocaListAdapter(this,R.layout.voca_list_item,showData);
//        vocaListView.setAdapter(adapter);
//    }

    @Override
    protected void onDestroy() {
        vocaData=null;
        vocaListView=null;
        vocaData=null;
        super.onDestroy();
    }

    // 사용자가 단어장에 새 단어 추가
    public void applyTexts(String eng, String kor) {
        VocaVO voca = new VocaVO(eng, kor, false, false);

        // 현재 루트: DB/users/uid/userVoca/선택된 단어장
        mDatabaseReference.updateChildren(voca.toMap());
        //adapter.notifyDataSetChanged();

        // 화면 갱신
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    // 달성률 얻어오기
    private void getCount(String uid)
    {
        today = getToday();

        mDatabaseReference = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(uid)
            .child("goals")
            .child(today)
            .child("count");

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = dataSnapshot.getValue(Integer.class);

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
}
