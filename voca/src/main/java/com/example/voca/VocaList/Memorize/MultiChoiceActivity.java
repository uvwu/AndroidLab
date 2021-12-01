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

public class MultiChoiceActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, View.OnClickListener {
    TextToSpeech tts;
    Button ttsBtn;
    TextView voca;
    String answer;
    Button multi1,multi2,multi3,multi4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_choice);

        //암기 버튼
        CheckBox memo = findViewById(R.id.memo_check_multi);
        memo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    showToast("암기 확인");
                else
                    showToast("암기 취소");
            }
        });

        //즐겨 찾기 버튼
        CheckBox star = findViewById(R.id.star_btn_multi);
        star.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    showToast("즐겨찾기 추가");
                else
                    showToast("즐겨찾기 삭제");
            }
        });
        voca=findViewById(R.id.voca_multi);
        answer="사과";
        multi1=findViewById(R.id.btn1_multi);
        multi1.setText("배");
        multi1.setOnClickListener(this);

        multi2=findViewById(R.id.btn2_multi);
        multi2.setText("사과");
        multi2.setOnClickListener(this);

        multi3=findViewById(R.id.btn3_multi);
        multi3.setText("딸기");
        multi3.setOnClickListener(this);

        multi4=findViewById(R.id.btn4_multi);
        multi4.setText("오렌지");
        multi4.setOnClickListener(this);

        //TTS 버튼
        tts=new TextToSpeech(this,this);
        ttsBtn=findViewById(R.id.tts_btn_multi);
        ttsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakOut();
            }
        });
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
    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
    @RequiresApi(api= Build.VERSION_CODES.LOLLIPOP)
    private void speakOut() {

        String text = voca.getText().toString();
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

    @Override
    public void onClick(View v) {
        Button b=(Button)v;
        if(((Button) v).getText().equals(answer))
            showToast("정답!");
        else showToast("오답");
    }
}