package com.example.voca.VocaList.Memorize.SpellCheck;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.voca.R;
import com.example.voca.VocaVO;

import java.util.ArrayList;

public class SpellCheckActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_check);

        viewPager2=findViewById(R.id.spellcheck_viewPager2);
        ArrayList<VocaVO> list=new ArrayList<>();
        list.add(new VocaVO("apple","사과",true,true));
        list.add(new VocaVO("banana","바나나",true,true));
        list.add(new VocaVO("water","물",true,true));


        viewPager2.setAdapter(new SpellCheckAdapter(list));
    }
}