package com.example.voca.Memorize.Basic;

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

public class MemorizeActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorize);

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

        Intent intent=getIntent();
        Bundle bundleData=intent.getBundleExtra("vocaData");
        ArrayList<VocaVO> list=new ArrayList<>();
        list.addAll(bundleData.getParcelableArrayList("vocaData"));

        viewPager2.setAdapter(new MemorizeAdapter(list,tts));

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