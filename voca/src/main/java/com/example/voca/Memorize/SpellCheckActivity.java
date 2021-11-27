package com.example.voca.Memorize;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.voca.R;

public class SpellCheckActivity extends AppCompatActivity {

    CheckBox memoCheck, star;//암기 여부 , 즐겨찾기
    TextView count, voca;// 단어 현재/전체, 보여지는 한글 뜻
    EditText editAnswerText;// 정답 입력
    Button checkBtn; //정답 체크 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_check);

        memoCheck=findViewById(R.id.memo_check_spell);
        memoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    showToast("암기 확인");
                else
                    showToast("암기 취소");
            }
        });

        star=findViewById(R.id.star_btn_spell);
        star.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    showToast("즐겨찾기 추가");
                else
                    showToast("즐겨찾기 삭제");
            }
        });

        count=findViewById(R.id.count_spell);

        voca=findViewById(R.id.voca_spell);

        editAnswerText=findViewById(R.id.text_answer_spell);

        String realAnswer="apple";
        checkBtn=findViewById(R.id.btn_check_spell);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(realAnswer.equals(editAnswerText.getText().toString()))
                    showToast("정답!");
                else showToast("오답!");
                editAnswerText.setText(null);
            }
        });


    }
    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}