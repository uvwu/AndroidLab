package com.example.voca.VocaList;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.voca.R;
import com.example.voca.realtimeDB.VocaVO;

import java.util.ArrayList;

public class VocaDialogVocaList extends AppCompatDialogFragment {
    private VocaDialogVocaListListener listener;
    private EditText editEng;
    private EditText editKor;

    ArrayList<VocaVO> vocaData;
    Context context;

    public VocaDialogVocaList(ArrayList<VocaVO> vocaData, Context context)
    {
        this.listener = null;
        this.editEng = null;
        this.editEng = null;
        this.vocaData = vocaData;
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =  getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_voca_list,null);

        builder.setView(view)
                .setTitle("단어 추가")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                })

                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String eng = editEng.getText().toString();

                        for(VocaVO voca: vocaData)
                        {
                            // 이미 동일한 영단어가 존재하면 생성되지 않음
                            if(eng.equals(voca.getVocaEng())){
                                Toast.makeText(context, "동일한 단어가 이미 존재합니다", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        String kor = editKor.getText().toString();

                        listener.applyTexts(eng, kor);
                    }
                });
        editEng = view.findViewById(R.id.edit_eng);
        editKor = view.findViewById(R.id.edit_kor);

        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (VocaDialogVocaListListener) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement VocaDialogListener");

        }
    }

    public interface VocaDialogVocaListListener{
        void applyTexts(String eng, String kor);

    }

}


