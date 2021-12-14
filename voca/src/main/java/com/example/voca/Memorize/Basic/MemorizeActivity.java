package com.example.voca.Memorize.Basic;


import android.content.Context;import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.voca.R;
import com.example.voca.realtimeDB.VocaVO;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

// TODO: 단어 암기여부 체크시 현재 화면에서 외운 단어 더이상 보이지 않게 하기 or 단어 무한 스와이프 가능하게 하기 (10번째 단어 끝나면 다시 1번째 단어 나타나게)
// TODO: tts 기능 작동 여부 확인
public class MemorizeActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    TextToSpeech tts;

    Context context;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase_count; // 데이터베이스의 주소 저장
    private DatabaseReference mDatabase_voca;
    private DatabaseReference mDatabase_star;

    String title;
    String today = getToday();
    //public Context memorizeActivityContext=getApplicationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorize);

        Intent intent=getIntent();
        Bundle bundleData=intent.getBundleExtra("vocaData");
        title = intent.getExtras().getString("title");

//        // 실시간 사용자의 정보 받아옴
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String uid = user.getUid();
//
//        // 실시간 데이터베이스 연결
//        firebaseDatabase = FirebaseDatabase.getInstance(); // 실시간 DB 관리 객체 얻어오기
//        mDatabase_count = firebaseDatabase.getReference("users")
//                                .child(uid)
//                                .child("goals")
//                                .child(today);
//
//

        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    int result = tts.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    } else {

                    }

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });
        viewPager2=findViewById(R.id.memorize_viewPager2);

        ArrayList<VocaVO> list=new ArrayList<>();
        list.addAll(bundleData.getParcelableArrayList("vocaData"));

        viewPager2.setAdapter(new MemorizeAdapter(list,tts, title));

    }
    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        super.onDestroy();
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
