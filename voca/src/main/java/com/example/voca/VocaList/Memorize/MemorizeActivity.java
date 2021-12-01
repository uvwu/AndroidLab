package com.example.voca.VocaList.Memorize;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.voca.R;

import java.util.Locale;

public class MemorizeActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    TextToSpeech tts;
    Button ttsBtn;
    TextView vocaTextView, koreanTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorize);

        vocaTextView = findViewById(R.id.voca);
        koreanTextView = findViewById(R.id.content);

        CheckBox memo, star;

        //암기 체크 박스 버튼
        memo = findViewById(R.id.memo_check);
        memo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    showToast("암기 확인");
                else
                    showToast("암기 취소");
            }
        });

        //즐겨 찾기 체크 박스 버튼
        star = findViewById(R.id.star_btn);
        star.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    showToast("즐겨찾기 추가");
                else
                    showToast("즐겨찾기 삭제");
            }
        });

        //TTS 버튼
        tts=new TextToSpeech(this,this);
        ttsBtn=findViewById(R.id.tts_btn);
        ttsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakOut();
            }
        });

    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                ttsBtn.setEnabled(true);
                //speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }
    @RequiresApi(api= Build.VERSION_CODES.LOLLIPOP)
    private void speakOut() {

        String text = vocaTextView.getText().toString();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH,null);
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