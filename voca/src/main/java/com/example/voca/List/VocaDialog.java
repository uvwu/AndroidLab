package com.example.voca.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.voca.R;

public class VocaDialog extends AppCompatDialogFragment  {
    private VocaDialogListener listener;
    private EditText editTextVocaname;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =  getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);

        builder.setView(view)
                .setTitle("단어장 추가")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                })

                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String vocaname = editTextVocaname.getText().toString();
                        listener.applyTexts(vocaname);
                    }
                });
        editTextVocaname = view.findViewById(R.id.edit_vocaname);
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (VocaDialogListener) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement VocaDialogListener");

        }
    }

    public interface VocaDialogListener{
        void applyTexts(String vocaname);

    }

}

