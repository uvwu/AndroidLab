package com.example.voca.Memorize.SpellCheck;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.voca.R;
import com.example.voca.realtimeDB.VocaVO;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SpellCheckActivity extends AppCompatActivity {
    ViewPager2 viewPager2;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseReference; // 데이터베이스의 주소 저장

    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_check);

        Intent intent=getIntent();
        Bundle bundleData=intent.getBundleExtra("vocaData");
        title = intent.getExtras().getString("title");

        viewPager2=findViewById(R.id.spellcheck_viewPager2);


        ArrayList<VocaVO> list=new ArrayList<>();
        list.addAll(bundleData.getParcelableArrayList("vocaData"));


        viewPager2.setAdapter(new SpellCheckAdapter(list, title));
    }
}