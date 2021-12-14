package com.example.voca.Memorize.MultiChoice;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.voca.R;
import com.example.voca.realtimeDB.VocaVO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

// TODO: 정답 체크시 자동으로 화면 넘어가도록 -> 토스트가 너무 늦게 떠서 답을 여러번 누르면 오답인지 정답인지 한참 기다려야 알 수 있음

public class MultiChoiceActivity extends AppCompatActivity{
    private static final String TAG = "MultiChoiceActivity";

    TextToSpeech tts;
    ViewPager2 viewPager2;

    // TODO: DB에 객관식에 나타날 한글들을 미리 235개 저장해뒀음
    // multiChoiceRandom 는 DB에 저장해뒀던 한글들을 다 모아둔 Array임
    ArrayList<String> multiChoiceRandom=new ArrayList<>(); // 객관식에 나타날 한글 모음

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseReference; // 데이터베이스의 주소 저장

    String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_choice);

        //단어 데이터 받아오기
        Intent intent=getIntent();
        Bundle bundleData=intent.getBundleExtra("vocaData");
        title = intent.getExtras().getString("title");

        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = firebaseDatabase.getReference().child("random").child("random_voca");

        ArrayList<VocaVO> list=new ArrayList<>();
        //list=bundleData.getParcelableArrayList("vocaData");
        list.addAll(bundleData.getParcelableArrayList("vocaData"));

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "addLin~: " + snapshot.getChildrenCount());
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String str = dataSnapshot.getValue().toString();
                    Log.d(TAG, "str: " + str);
                    multiChoiceRandom.add(str);
                    Log.d(TAG, "multiChoiceRandom: " + multiChoiceRandom);
                }
                Collections.shuffle(Arrays.asList(multiChoiceRandom));
                viewPager2.setAdapter(new MultiChioceAdapter(list,tts, title,multiChoiceRandom));
                // TODO: MutiChoiceViewHolderPage에 multiChoiceRandom 넘기는 코드는 여기 작성!
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

        viewPager2=findViewById(R.id.multichoice_viewPager2);



        /*Collections.shuffle(Arrays.asList(multiChoiceRandom));
        ArrayList<String> multi4=new ArrayList<>();
        multi4.add(multiChoiceRandom.get(0).toString());
        multi4.add(multiChoiceRandom.get(1).toString());
        multi4.add(multiChoiceRandom.get(2).toString());

        viewPager2.setAdapter(new MultiChioceAdapter(list,tts, title,multi4));*/
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


}