package com.example.voca.List;

import static com.google.firebase.database.core.RepoManager.clear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voca.R;

import java.util.ArrayList;

public class VocaDetailProvidedAdapter extends RecyclerView.Adapter<VocaDetailProvidedAdapter.ItemViewHolder>{
    Context context;
    ArrayList<VocaDetailProvidedItem> items;
    int resId;

    public VocaDetailProvidedAdapter(Context context, int resId, ArrayList<VocaDetailProvidedItem> items) {
        this.context = context;
        this.resId = resId;
        this.items = items;
    }

    @NonNull
    @Override
    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.vocadetail_item, parent, false);
        VocaDetailProvidedAdapter.ItemViewHolder vh = new VocaDetailProvidedAdapter.ItemViewHolder(view);

        return vh;
    }

    @Override
    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        VocaDetailProvidedItem item = items.get(position);

        holder.Eng.setText(item.getEng());
        holder.Kor.setText(item.getKor());
    }

    @Override
    public int getItemCount() {
        return items.size();
    } // 데이터 전체 개수 리턴

    // 아이템 뷰를 저장하는 뷰 홀더 클래스
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView Eng;
        public TextView Kor;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조
            context = itemView.getContext();
            Eng = itemView.findViewById(R.id.eng);
            Kor = itemView.findViewById(R.id.kor);
        }

    }
}