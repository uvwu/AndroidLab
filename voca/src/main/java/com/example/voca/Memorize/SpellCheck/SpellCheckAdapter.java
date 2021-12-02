package com.example.voca.Memorize.SpellCheck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voca.R;
import com.example.voca.VocaVO;

import java.util.ArrayList;

public class SpellCheckAdapter extends RecyclerView.Adapter<SpellCheckViewHolderPage> {
    private ArrayList<VocaVO> listVoca;

    SpellCheckAdapter(ArrayList<VocaVO> data){
        this.listVoca=data;
    }

    @Override
    public SpellCheckViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.item_spellcheck_viewpager,parent,false);
        return new SpellCheckViewHolderPage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpellCheckViewHolderPage holder, int position) {
        if(holder instanceof SpellCheckViewHolderPage){
            SpellCheckViewHolderPage viewHolderPage=(SpellCheckViewHolderPage) holder;
            viewHolderPage.onBind(listVoca.get(position),getItemCount());
        }
    }

    @Override
    public int getItemCount() {
        return listVoca.size();
    }
}
