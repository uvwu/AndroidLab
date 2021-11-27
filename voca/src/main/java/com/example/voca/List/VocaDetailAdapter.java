package com.example.voca.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voca.R;

import java.util.ArrayList;

public class VocaDetailAdapter  extends RecyclerView.Adapter<VocaDetailAdapter.ItemViewHolder> implements ItemTouchHelperListener {

    private ArrayList<VocaDetailItem> items = new ArrayList<>();

    public VocaDetailAdapter() {

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.vocadetail_item, parent, false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<VocaDetailItem> itemList) {
        items = itemList;
        notifyDataSetChanged();
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        VocaDetailItem item = items.get(from_position);
        items.remove(from_position);
        items.add(to_position, item);
        // item.setNumber(to_position);
        notifyItemMoved(from_position, to_position);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView Eng;
        TextView Kor;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            Eng = itemView.findViewById(R.id.eng);
            Kor = itemView.findViewById(R.id.kor);
        }

        public void onBind(VocaDetailItem item, int position) {
            Eng.setText(item.getEng());
            Kor.setText(item.getKor());
            //item.setNumber(position);
        }
    }
}

