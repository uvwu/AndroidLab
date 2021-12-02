package com.example.voca.Memorize.MultiChoice;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voca.R;
import com.example.voca.VocaVO;

import java.util.ArrayList;

public class MultiChioceAdapter extends RecyclerView.Adapter<MultiChoiceViewHolderPage> {
    private ArrayList<VocaVO> listVoca;
    TextToSpeech tts;

    MultiChioceAdapter(ArrayList<VocaVO> data, TextToSpeech tts){
        this.listVoca=data;
        this.tts=tts;
    }

    @NonNull
    @Override
    public MultiChoiceViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.item_multichoice_viewpager,parent,false);
        return new MultiChoiceViewHolderPage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiChoiceViewHolderPage holder, int position) {
        if(holder instanceof MultiChoiceViewHolderPage){
            MultiChoiceViewHolderPage viewHolderPage=(MultiChoiceViewHolderPage) holder;
            viewHolderPage.onBind(listVoca.get(position),getItemCount());
            viewHolderPage.ttsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tts.speak(viewHolderPage.vocaEng.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listVoca.size();
    }
}
