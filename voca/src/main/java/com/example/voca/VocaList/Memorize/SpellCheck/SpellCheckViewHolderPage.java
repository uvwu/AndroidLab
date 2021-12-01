package com.example.voca.VocaList.Memorize.SpellCheck;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.voca.R;
import com.example.voca.VocaVO;

public class SpellCheckViewHolderPage extends RecyclerView.ViewHolder {

    private final TextView vocaKor;
    private CheckBox memoCheck;
    private CheckBox starCheck;
    private EditText editAnswerText;
    private Button checkBtn;
    private TextView count;
    VocaVO vo;


    SpellCheckViewHolderPage(View v){
        super(v);

        vocaKor=v.findViewById(R.id.voca_korean2);
        memoCheck=v.findViewById(R.id.memo_check_spell2);
        starCheck=v.findViewById(R.id.star_btn_spell2);
        editAnswerText=v.findViewById(R.id.text_answer_spell2);
        checkBtn=v.findViewById(R.id.btn_check_spell2);
        count=v.findViewById(R.id.count_spell2);
    }
    public void onBind(VocaVO vo,int toalNum){
        this.vo=vo;

        //암기버튼 이벤트 처리리
       memoCheck.setChecked(vo.memoCheck);// memoCheck 값의 여부에 따라 화면에 체크 유무 보여줌
        memoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    vo.memoCheck=true;
                }
                else{
                    vo.memoCheck=false;
                }
            }
        });
        //즐겨찾기 버튼 이벤트 처리
        starCheck.setChecked(vo.starCheck);// starCheck 값의 여부에 따라 화면에 체크 유무 보여줌
        starCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    vo.starCheck=true;
                }
                else{
                    vo.starCheck=false;
                }
            }
        });

        // 현재단어/전체단어 ex) 3/20
        count.setText(getAdapterPosition()+1+"/"+toalNum);

        String realAnswer=vo.vocaEng;
        vocaKor.setText(vo.vocaKor);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(realAnswer.equals(editAnswerText.getText().toString())) {
                    Toast.makeText(v.getContext(),"정답",Toast.LENGTH_SHORT).show();
                    editAnswerText.setText(null);
                }
                else {
                }
            }
        });

    }
}
