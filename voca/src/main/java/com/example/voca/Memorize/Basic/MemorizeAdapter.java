package com.example.voca.Memorize.Basic;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voca.R;
import com.example.voca.realtimeDB.VocaVO;

import java.util.ArrayList;


public class MemorizeAdapter extends RecyclerView.Adapter<MemorizeViewHolderPage> {

    private ArrayList<VocaVO> listVoca;
    TextToSpeech tts;

    MemorizeAdapter(ArrayList<VocaVO> data,TextToSpeech tts){
        this.listVoca=data;
        this.tts=tts;
    }

    @NonNull
    @Override
    public MemorizeViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.item_memorize_viewpager,parent,false);
        return new MemorizeViewHolderPage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemorizeViewHolderPage holder, int position){
        if(holder instanceof MemorizeViewHolderPage){
            MemorizeViewHolderPage viewHolderPage=(MemorizeViewHolderPage) holder;
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
