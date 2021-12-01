package com.example.voca.Memorize;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.voca.VocaVO;

import java.util.ArrayList;

public class VocaAdapter extends ArrayAdapter<VocaVO> {
    Context context;
    int resId;
    ArrayList<VocaVO> data;
    public VocaAdapter(Context context,int resId,ArrayList<VocaVO> data){
        super(context, resId);
        this.context=context;
        this.resId=resId;
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, null);
            VocaHolder holder = new VocaHolder(convertView);
            convertView.setTag(holder);
        }

        VocaHolder holder=(VocaHolder)convertView.getTag();

        CheckBox memoCheck=holder.memoCheck;
        TextView vocaEng= holder.vocaEng;
        TextView vocaKor= holder.vocaKor;
        CheckBox starCheck=holder.starCheck;

        final VocaVO vo= data.get(position);

        //암기여부 버튼 이벤트 처리
        memoCheck.setChecked(vo.memoCheck);// memoCheck 값의 여부에 따라 화면에 체크 유무 보여줌
        memoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    vo.memoCheck=true;
                    Toast toast = Toast.makeText(context, "암기 확인", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    vo.memoCheck=false;
                    Toast toast = Toast.makeText(context, "암기 취소", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        vocaEng.setText(vo.vocaEng);//영어단어 보여주기
        vocaKor.setText(vo.vocaKor);//영어단어 한글 뜻 보여주기

        //즐겨찾기 버튼 이벤트 처리
        starCheck.setChecked(vo.starCheck);// starCheck 값의 여부에 따라 화면에 체크 유무 보여줌
        starCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    vo.starCheck=true;
                    Toast toast = Toast.makeText(context, "즐겨찾기 추가", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    vo.starCheck=false;
                    Toast toast = Toast.makeText(context, "즐겨찾기 삭제", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        return convertView;
    }
}
