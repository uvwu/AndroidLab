package com.example.voca.VocaList.Memorize.Basic;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.voca.R;
import com.example.voca.VocaVO;

public class MemorizeViewHolderPage extends RecyclerView.ViewHolder{
    TextView vocaEng;
    private final TextView vocaKor;
    private CheckBox memoCheck;
    private CheckBox starCheck;
    private EditText editAnswerText;
    private TextView count;
    Button ttsBtn;
    VocaVO vo;

    MemorizeViewHolderPage(View v){
        super(v);
        vocaEng=v.findViewById(R.id.voca);
        vocaKor=v.findViewById(R.id.content);
        memoCheck=v.findViewById(R.id.memo_check);
        starCheck=v.findViewById(R.id.star_btn);
        count=v.findViewById(R.id.count);
        ttsBtn=v.findViewById(R.id.tts_btn);
    }
    public void onBind(VocaVO vo,int toalNum) {
        this.vo = vo;

        //암기버튼 이벤트 처리리
        memoCheck.setChecked(vo.memoCheck);// memoCheck 값의 여부에 따라 화면에 체크 유무 보여줌
        memoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    vo.memoCheck = true;
                } else {
                    vo.memoCheck = false;
                }
            }
        });
        //즐겨찾기 버튼 이벤트 처리
        starCheck.setChecked(vo.starCheck);// starCheck 값의 여부에 따라 화면에 체크 유무 보여줌
        starCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    vo.starCheck = true;
                } else {
                    vo.starCheck = false;
                }
            }
        });

        // 현재단어/전체단어 ex) 3/20
        count.setText(getAdapterPosition() + 1 + "/" + toalNum);
        vocaEng.setText(vo.vocaEng);
        vocaKor.setText(vo.vocaKor);

    }
}
