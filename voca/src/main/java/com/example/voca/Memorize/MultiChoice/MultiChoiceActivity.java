package com.example.voca.Memorize.MultiChoice;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.voca.R;
import com.example.voca.VocaVO;

import java.util.ArrayList;
import java.util.Locale;

public class MultiChoiceActivity extends AppCompatActivity{
    TextToSpeech tts;
    ViewPager2 viewPager2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_choice);

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

        //단어 데이터 받아오기
        Intent intent=getIntent();
        Bundle bundleData=intent.getBundleExtra("vocaData");
        ArrayList<VocaVO> list=new ArrayList<>();
        list=bundleData.getParcelableArrayList("vocaData");
        //list.addAll(bundleData.getParcelableArrayList("vocaData"));


        viewPager2.setAdapter(new MultiChioceAdapter(list,tts));
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