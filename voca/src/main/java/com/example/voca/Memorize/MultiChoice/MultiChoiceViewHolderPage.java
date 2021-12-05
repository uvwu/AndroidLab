package com.example.voca.Memorize.MultiChoice;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.voca.R;
import com.example.voca.realtimeDB.VocaVO;

public class MultiChoiceViewHolderPage extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView vocaEng;
    private CheckBox memoCheck;
    private CheckBox starCheck;
    private TextView count;
    private Button chioceBtn1;
    private Button chioceBtn2;
    private Button chioceBtn3;
    private Button chioceBtn4;
    Button ttsBtn;

    private String realAnswer;//정답인 한글 뜻

    VocaVO vo;


    public MultiChoiceViewHolderPage(View v) {
        super(v);
        vocaEng=v.findViewById(R.id.voca_multi);
        memoCheck=v.findViewById(R.id.memo_check_multi);
        starCheck=v.findViewById(R.id.star_btn_multi);
        count=v.findViewById(R.id.count_multi);
        ttsBtn=v.findViewById(R.id.tts_btn_multi);
        chioceBtn1=v.findViewById(R.id.btn1_multi);
        chioceBtn2=v.findViewById(R.id.btn2_multi);
        chioceBtn3=v.findViewById(R.id.btn3_multi);
        chioceBtn4=v.findViewById(R.id.btn4_multi);
    }
    public void onBind(VocaVO vo,int totalNum) {
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
        count.setText(getAdapterPosition() + 1 + "/" + totalNum);
        vocaEng.setText(vo.vocaEng);

        //객관식 버튼(1~4) 값 넣는곳

        realAnswer=vo.vocaKor;//정답인 한글 뜻

        chioceBtn1.setText("1");
        chioceBtn1.setOnClickListener(this);

        chioceBtn2.setText("2");
        chioceBtn2.setOnClickListener(this);

        chioceBtn3.setText("3");
        chioceBtn3.setOnClickListener(this);

        chioceBtn4.setText(realAnswer);
        chioceBtn4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Button btn=(Button) v;
        if(btn.getText().toString().equals(realAnswer)){
            Toast.makeText(v.getContext(),"정답",Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(v.getContext(),"오답",Toast.LENGTH_SHORT).show();
    }


}
