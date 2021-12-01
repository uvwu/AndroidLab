package com.example.voca.VocaList;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.voca.R;

public class VocaHolder {
    public CheckBox memoCheck;
    public TextView vocaEng;
    public TextView vocaKor;
    public CheckBox starCheck;
    public VocaHolder(View root){
        memoCheck=root.findViewById(R.id.voca_list_item_memo_check);
        vocaEng=root.findViewById(R.id.voca_list_item_voca_eng);
        vocaKor=root.findViewById(R.id.voca_list_item_voca_kor);
        starCheck=root.findViewById(R.id.voca_list_item_star_check);
    }
}
